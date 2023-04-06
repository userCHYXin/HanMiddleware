package com.middleware.redistest.entity;

import java.math.BigDecimal;
import java.util.Date;

public class RedDetail {
    private Integer id;                                       //主键id
    private Integer recordId;                                 //红包记录id
    private BigDecimal amount;                                //红包随机金额
    private Byte isActive;                                    //是否有效
    private Date createTime;                                  //创建时间
    //此处省略每个字段的getter、setter方法 MARK

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId(){
        return id;
    }
    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
    public Byte getIsActive(){
        return isActive;
    }
    public Integer getRecordID(){
        return recordId;
    }
    public void setRecordId(Integer recordId){
        this.recordId = recordId;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public Date getCreateTime(){
        return createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }
}
