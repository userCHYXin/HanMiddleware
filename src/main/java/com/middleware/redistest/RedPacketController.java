package com.middleware.redistest;

import java.awt.PageAttributes.MediaType;
import java.math.BigDecimal;

import com.middleware.redistest.dto.RedPacketDto;
import com.middleware.redistest.service.IRedPacketService;
import com.middleware.utils.BaseResponse;
import com.middleware.utils.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 红包处理逻辑Controller
 *
 * @author: debug
 */
@RestController
public class RedPacketController {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RedPacketController.class);
    //定义请求路径的前缀
    private static final String prefix = "red/packet";
    //注入红包业务逻辑处理接口服务
    @Autowired
    private IRedPacketService redPacketService;

    /**
     * 发红包请求-请求方法为Post，请求参数采用JSON格式进行交互
     */
    @RequestMapping(value = prefix + "/hand/out", method = RequestMethod.POST)
    public BaseResponse handOut(@Validated @RequestBody RedPacketDto dto, BindingResult result) {
        //参数校验
        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //核心业务逻辑处理服务-最终返回红包全局唯一标识串
            String redId = redPacketService.handOut(dto);
            //将红包全局唯一标识串返回给前端
            response.setData(redId);
        } catch (Exception e) {
            //如果报异常则打印日志并返回相应的错误信息
            log.error("发红包发生异常：dto={} ", dto, e.fillInStackTrace());
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
    /**
     * 处理抢红包请求：接收当前用户账户id和红包全局唯一标识串参数
     */
    @RequestMapping(value = prefix+"/rob",method = RequestMethod.GET)
    public BaseResponse rob(@RequestParam Integer userId, @RequestParam String
            redId){
        //定义响应对象
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try {
            //调用红包业务逻辑处理接口中的抢红包方法，最终返回抢到的红包金额
            //单位为元（不为Null时则表示抢到了，否则代表已经被抢完了）
            BigDecimal result=redPacketService.rob(userId,redId);
            if (result!=null){
                //将抢到的红包金额返回到前端
                response.setData(result);
            }else{
                //没有抢到红包，即已经被抢完了
                response=new BaseResponse(StatusCode.Fail.getCode(),"红包已被抢完!");
            }
        }catch (Exception e){
            //处理过程如果发生异常，则打印异常信息并返回给前端
            log.error("抢红包发生异常：userId={} redId={}",userId,redId,e.fillInStackTrace());
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}
