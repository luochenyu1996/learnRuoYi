package com.chenyu.common.annotation;

import java.lang.annotation.*;

/**
 * @program: learnRuoYi
 * @description: 数据权限范围注解
 * @author: chen yu
 * @create: 2021-10-05 17:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 部门表的别名
     *
     */
    public  String deptAlias() default "";


    /**
     * 用户表别名
     *
     */
    public String userAlias() default "";



}