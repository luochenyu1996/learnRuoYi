package com.chenyu.common.exception.user;

import com.chenyu.common.exception.base.BaseException;

/**
 * @program: learnRuoYi
 * @description: 用户异常
 * @author: chen yu
 * @create: 2021-10-01 21:43
 */
public class UserException extends BaseException {

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }

}