package com.middleware.utils;

public class BaseResponse<T> {
    private Integer code;                 //状态码
    private String msg;                         //描述信息
    private T data;                                 //响应数据-采用泛型表示可以接受通用的数据类型
    public BaseResponse(Integer code, String msg) {         //重载的构造方法一
        this.code = code;
        this.msg = msg;
    }
    public BaseResponse(StatusCode statusCode) {                 //重载的构造方法二
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }
    public BaseResponse(Integer code, String msg, T data) {
        //重载的构造方法三
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
