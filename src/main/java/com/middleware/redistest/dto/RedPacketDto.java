package com.middleware.redistest.dto;
//导入包
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
/**
 * 发红包请求时接收的参数对象
 * @author: debug
 */
@Data
@ToString
public class RedPacketDto {
    private Integer userId;                         //用户账号id
    @NotNull
    private Integer total;                          //红包个数
    @NotNull
    private Integer amount;                         //总金额-单位为分
}
