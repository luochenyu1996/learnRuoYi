package com.chenyu.common.constant;

/**
 * @program: learnRuoYi
 * @description:  用户常量信息
 * @author: chen yu
 * @create: 2021-10-02 17:26
 */
public class UserConstants {



    /**
     * 用户名称长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 用户密码长度限制
     */
    public static final int  PASSWORD_MIN_LENGTH=5;
    public static final int  PASSWORD_MAX_LENGTH=20;


    /**
     * 表示用户在系统中存在  0 不存在 1 存在
     */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";

}