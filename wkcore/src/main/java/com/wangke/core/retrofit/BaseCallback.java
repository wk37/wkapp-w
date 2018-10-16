package com.wangke.core.retrofit;


import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class BaseCallback<T> implements Callback<BaseEntity<T>> {

    @Override
    public void onResponse(Call<BaseEntity<T>> call, Response<BaseEntity<T>> response) {
        try {
            if (response.body().getCode() == 200) {
                onSuccess(200, response.body().getData());
            } else {
                onError(response.body().getCode(), response.body().getMsg());

            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(call, e);
        }
    }

    @Override
    public void onFailure(Call<BaseEntity<T>> call, Throwable e) {
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


    public abstract void onSuccess(int code, T data);

    public abstract void onError(int code, String msg);

}
