package com.chenyu.common.exception.user;

/**
 * @program: learnRuoYi
 * @description: 验证码错误
 * @author: chen yu
 * @create: 2021-10-01 21:50
 */
public class CaptchaException  extends  UserException{
    public CaptchaException() {
        super("user.jcaptcha.expire", null);
    }
}