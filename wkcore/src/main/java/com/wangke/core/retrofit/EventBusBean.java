package com.wangke.core.retrofit;

/**
 * Created by wking on 2017/3/26.
 */

public class EventBusBean<T> {


    private Object tag;
    private int code;
    private  T data;

    public EventBusBean(Object tag, int code, T data) {
        this.tag = tag;
        this.code = code;
        this.data = data;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
