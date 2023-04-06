package com.middleware.redistest.service;

import com.middleware.redistest.dto.RedPacketDto;

import java.math.BigDecimal;

public interface IRedPacketService {
    //发红包核心业务逻辑的实现
    String handOut(RedPacketDto dto) throws Exception;
    //抢红包，在下一章中将实现该方法
    BigDecimal rob(Integer userId,String redId) throws Exception;
}

