package com.wangke.core.retrofit;

/**
 * Created by wk37 on 2017/3/24.
 */

public class BaseEntity<T> {


    private T data;
    private int code;
    private String msg;
    private String timestamp ="";

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}