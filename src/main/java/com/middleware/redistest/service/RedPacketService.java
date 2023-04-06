package com.middleware.redistest.service;


import com.middleware.redistest.algorithm.RedPacketAlgorithm;
import com.middleware.redistest.dto.RedPacketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 红包业务逻辑处理接口-实现类
 *
 * @author: debug
 */
@Service
public class RedPacketService implements IRedPacketService {

    //日志
    private static final Logger log = LoggerFactory.getLogger(RedPacketService.class);
    //存储至缓存系统Redis时定义的Key前缀
    private static final String keyPrefix = "redis:red:packet:";
    //定义Redis操作Bean组件
    @Autowired
    private RedisTemplate redisTemplate;
    //自动注入红包业务逻辑处理过程数据记录接口服务
    @Autowired
    private IRedService redService;

    /**
     * 发红包
     *
     * @throws Exception
     */
    @Override
    public String handOut(RedPacketDto dto) throws Exception {
        //判断参数的合法性
        if (dto.getTotal() > 0 && dto.getAmount() > 0) {
            //采用二倍均值法生成随机金额列表，在上一节已经采用代码实现了二倍均值法
            List<Integer> list = RedPacketAlgorithm.divideRedPackage(dto.getAmount(),
                    dto.getTotal());
            //生成红包全局唯一标识串
            String timestamp = String.valueOf(System.nanoTime());
            //根据缓存Key的前缀与其他信息拼接成一个新的用于存储随机金额列表的Key
            String redId = new StringBuffer(keyPrefix).append(dto.getUserId()).
                    append(":").append(timestamp).toString();
            //将随机金额列表存入缓存List中
            redisTemplate.opsForList().leftPushAll(redId, list);
            //根据缓存Key的前缀与其他信息拼接成一个新的用于存储红包总数的Key
            String redTotalKey = redId + ":total";
            //将红包总数存入缓存中
            redisTemplate.opsForValue().set(redTotalKey, dto.getTotal());
            //缓存了两个redis键值对 一个是红包总数 一个是红包列表 然后异步存储红包列表
            //异步记录红包的全局唯一标识串、红包个数与随机金额列表信息至数据库中
            redService.recordRedPacket(dto, redId, list);
            //将红包的全局唯一标识串返回给前端
            return redId;
        } else {
            throw new Exception("系统异常-分发红包-参数不合法!");
        }
    }

    /**
     * 抢红包实际业务逻辑处理
     *
     * @param userId 当前用户id-抢红包者
     * @param redId  红包全局唯一标识串
     * @return 返回抢到的红包金额或者抢不到红包金额的Null
     * @throws Exception
     */
    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        //定义Redis操作组件的值操作方法
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //在处理用户抢红包之前，需要先判断一下当前用户是否已经抢过该红包了
        //如果已经抢过了，则直接返回红包金额，并在前端显示出来
        Object obj = valueOperations.get(redId + userId + ":rob");
        if (obj != null) {
            return new BigDecimal(obj.toString());
        }
        //“点红包”业务逻辑-主要用于判断缓存系统中是否仍然有红包，即//红包剩余个数是否大于0
        Boolean res = click(redId);
        if (res) {
            // res为true，则可以进入“拆红包”业务逻辑的处理
            //从小红包随机金额列表中弹出一个随机金额
            //构造分布式锁 一个人红包只能抢到一次随机金额 保证一对一的关系
            //通过redis分布式锁解决问题
            //构造缓存中的key
            final String localKey = redId + userId + "-lock";
            Boolean lock = valueOperations.setIfAbsent(localKey,redId);
            redisTemplate.expire(localKey, 24L, TimeUnit.HOURS); //设置过期时间
            try{
                if(lock){
                    Object value = redisTemplate.opsForList().rightPop(redId);
                    if (value != null) {
                        //value!=null，表示当前弹出的红包金额不为Null，即有钱
                        //当前用户抢到一个红包了，则可以进入后续的更新缓存，并将信息记入数据库
                        String redTotalKey = redId + ":total";
                        //更新缓存系统中剩余的红包个数，即红包个数减1
                        Integer currTotal = valueOperations.get(redTotalKey) != null ? (Integer)
                                valueOperations.get(redTotalKey) : 0;
                        valueOperations.set(redTotalKey, currTotal - 1);
                        //将红包金额返回给用户前，在这里金额的单位设置为“元”
                        //如果你不想设置，则可以直接返回value，但是前端需要作除以100的操作
                        //因为在发红包业务模块中金额的单位是设置为“分”的
                        BigDecimal result = new BigDecimal(value.toString()).divide(new
                                BigDecimal(100));
                        //将抢到红包时用户的账号信息及抢到的金额等信息记入数据库
                        redService.recordRobRedPacket(userId, redId, new BigDecimal
                                (value.toString()));
                        //将当前抢到红包的用户设置进缓存系统中，用于表示当前用户已经抢过红包了
                        valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
                        //打印当前用户抢到红包的记录信息
                        log.info("当前用户抢到红包了：userId={} key={} 金额={} ", userId,
                                redId, result);
                        //将结果返回
                        return result;
                    }
                }
            }catch (Exception e){
                throw new Exception("系统异常，抢红包，分布式加锁失败");
            }
        }
        //null表示当前用户没有抢到红包
        return null;
    }

    /**
     * 点红包的业务处理逻辑-如果返回true，则代表缓存系统Redis还有红包，即剩余个数>0
     * 否则，意味着红包已经被抢光了
     *
     * @throws Exception
     */
    private Boolean click(String redId) throws Exception {
        //定义Redis的Bean操作组件-值操作组件
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //定义用于查询缓存系统中红包剩余个数的Key
        //这在发红包业务模块中已经指定过了
        String redTotalKey = redId + ":total";
        //获取缓存系统Redis中红包剩余个数
        Object total = valueOperations.get(redTotalKey);
        //判断红包剩余个数total是否大于0，如果大于0，则返回true，代表还有红包
        if (total != null && Integer.valueOf(total.toString()) > 0) {
            return true;
        }
        //返回false，代表已经没有红包可抢了
        return false;
    }



}