package com.chenyu.common.exception.user;

/**
 * @program: learnRuoYi
 * @description: 验证码过期
 * @author: chen yu
 * @create: 2021-10-01 21:47
 */
public class CaptchaExpireException extends  UserException {
    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}