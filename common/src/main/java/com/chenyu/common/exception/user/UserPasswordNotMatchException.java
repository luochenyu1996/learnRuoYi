package com.chenyu.common.exception.user;

/**
 * @program: learnRuoYi
 * @description: 用户密码错误或则不符合规范异常
 * @author: chen yu
 * @create: 2021-10-01 22:33
 */
public class UserPasswordNotMatchException  extends UserException {
    public UserPasswordNotMatchException() {
            super("user.password.not.match", null);
    }
}