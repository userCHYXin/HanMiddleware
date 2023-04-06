package com.middleware.redistest.entity;

import java.math.BigDecimal;
import java.util.Date;

//抢到红包时金额等相关信息记录表
public class RedRobRecord {
    private Integer id;                                     //主键id
    private Integer userId;                                 //用户id
    private String redPacket;                               //红包全局唯一标识串
    private BigDecimal amount;                              //抢到的红包金额
    private Date robTime;                                   //抢到时间
    private Byte isActive;                                  //是否有效
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public void setRedPacket(String redPacket){
        this.redPacket = redPacket;
    }
    public String getRedPacket(){
        return redPacket;
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
    public Byte getIsActive(){
        return isActive;
    }
    public void setIsActive(Byte isActive){
        this.isActive = isActive;
    }
    public void setRobTime(Date robTime){
        this.robTime = robTime;
    }
    public Date getRobTimeTime(){
        return robTime;
    }
}
