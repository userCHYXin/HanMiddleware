package com.middleware.redistest.entity;

import java.math.BigDecimal;
import java.util.Date;
//导入包
//发红包记录实体类
public class RedRecord {
    private Integer id;                                     //主键id
    private Integer userId;                                 //用户id
    private String redPacket;                               //红包全局唯一标识串
    private Integer total;                                  //红包指定可以抢的总人数
    private BigDecimal amount;                              //红包总金额
    private Byte isActive;                                  //是否有效
    private Date createTime;                                //创建时间
    //这里省略字段的getter、setter方法
    //Mark 省略的相应方法
    public Byte getIsActive(){
        return isActive;
    }
    public void setIsActive(Byte isActive){
        this.isActive = isActive;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return id;
    }
    public void setUserId(Integer userId){
        this.userId = userId;
    }
    public Integer getUserId(){
        return userId;
    }
    public void setRedPacket(String redPacket){
        this.redPacket = redPacket;
    }
    public String getRedPacket(){
        return redPacket;
    }
    public void setTotal(Integer total){
        this.total = total;
    }
    public Integer getTotal(){
        return total;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }
    public Date getCreateTime(){
        return createTime;
    }


}
