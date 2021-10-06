package com.chenyu.common.exception;

/**
 * @program: learnRuoYi
 * @description:
 * @author: chen yu
 * @create: 2021-10-02 11:17
 */
public class UtilException  extends  RuntimeException{

    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}