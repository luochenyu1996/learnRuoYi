package com.chenyu.common.exception;

/**
 * @program: learnRuoYi
 * @description: 业务异常
 * @author: chen yu
 * @create: 2021-10-01 22:36
 */
public class ServiceException  extends  RuntimeException {



    //错误码
    private Integer code;

    //错误提示
    private String message;

    //错误明细
    private String detailMessage;






    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }


}