package com.chenyu.common.enums;

/**
 * @program: learnRuoYi
 * @description: 用户状体枚举类
 * @author: chen yu
 * @create: 2021-10-01 11:45
 */
public enum UserStats {
    OK("0","正常"),
    DISABLE("1","停用"),
    DELETED("2","删除");

    private  final String code;
    private final  String info;

    UserStats(String  code,String  info){
        this.code=code;
        this.info=info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}