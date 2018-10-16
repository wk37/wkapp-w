package com.wangke.core.retrofit;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class BaseObserver<T> extends DisposableObserver<BaseEntity<T>> {

    @Override
    public void onNext(BaseEntity<T> tBaseEntity) {
        try {
            if (tBaseEntity.getCode() == 200) {
                onSuccess(200, tBaseEntity.getData());
            } else {
                onError(tBaseEntity.getCode(), tBaseEntity.getMsg());

            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        String errorMsg = "异常错误";
        int code = -1;
        if (e instanceof HttpException) {     //   HTTP错误
            code =( (HttpException)e).code();
            errorMsg =e.getMessage();
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            errorMsg = "连接错误";
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            errorMsg = "连接超时";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            errorMsg = "解析错误";
        }
        onError(code, errorMsg);


    }

    @Override
    public void onComplete() {

    }


    public abstract void onSuccess(int code, T data);

    public abstract void onError(int code, String msg);

}
