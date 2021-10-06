package com.chenyu.common.constant;



/**
 * @program: learnRuoYi
 * @description: 通用常量信息
 * @author: chen yu
 * @create: 2021-10-01 19:14
 */
public class Constants {
    public static final String TOKEN = "token";

    public static final String UTF8 = "UTF-8";

    public static final String GBK = "GBK";



    //令牌前缀
    public static final String TOKEN_PREFIX = "Bearer ";


    //登录用户的 redis key
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    //验证码  redis 的key
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    public static final String LOGIN_USER_KEY = "login_user_key";



    //验证码有效时间（分钟）
    public static final Integer CAPTCHA_EXPIRATION = 2;


}