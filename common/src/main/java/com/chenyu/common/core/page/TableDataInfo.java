package com.chenyu.common.core.page;

import java.util.List;

/**
 * @program: learnRuoYi
 * @description: 表格分页数据对象
 * @author: chen yu
 * @create: 2021-10-05 12:55
 */
public class TableDataInfo {



    //总挤纪录数
    private long total;


    //列表数据
    private List<?> rows;


    //消息状态吗
    private int code;


    //消息内容
    private String msg;

    //表格数据对象
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}