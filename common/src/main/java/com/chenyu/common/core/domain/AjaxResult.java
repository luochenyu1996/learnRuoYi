package com.chenyu.common.core.domain;

import com.chenyu.common.constant.HttpStatus;
import com.chenyu.common.utils.StringUtils;

import java.util.HashMap;

/**
 * @program: learnRuoYi
 * @description: 接口返回数据
 * @author: chen yu
 * @create: 2021-10-01 13:20
 */
public class AjaxResult extends HashMap<String, Object> {

    //状态码
    public static final String CODE_TAG = "code";

    //返回内容

    public static final String MSG_TAG = "msg";


    //数据对象
    public static final String DATA_TAG = "data";

    public AjaxResult() {
    }

    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }


    public AjaxResult(int code, String msg, Object obj) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (!StringUtils.isNull(obj)) {
            super.put(DATA_TAG, obj);
        }
    }


    /**
     * 返回成功消息
     *
     * @return 返回成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }


    /**
     * 返回成功消息
     *
     * @param msg 消息内容
     * @return 返回成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }


    /**
     * 返回成功消息
     *
     * @param object 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(Object object) {
        return AjaxResult.success("操作成功", object);
    }


    /**
     * 返回成功消息
     *
     * @param msg  消息内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }


    /**
     * 返回错误消息
     *
     * @return 错误消息
     */

    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }


    /**
     * 返回错误消息
     *
     * @param msg 消息内容
     * @return 错误消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  消息内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }


    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }


}