package com.wangke.core.retrofit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Application 执行 init 方法，然后 各模块 新建子类继承该类，通过 泛型T 方法getTApi(T)实现各模块 api 的创建
 */
public class BaseRetrofit {

    private static String mBaseUrl;
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static Retrofit build;


    public static void init(String baseUrl, long time, boolean isDebug) {
        mBaseUrl = baseUrl;
        initOkHttp(time, isDebug);
    }


    private static void initOkHttp(long time, boolean isDebug) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(time, TimeUnit.MILLISECONDS)
                    .writeTimeout(time, TimeUnit.MILLISECONDS)
                    .connectTimeout(time, TimeUnit.MILLISECONDS);

            if (isDebug) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClient = builder
                        .addInterceptor(logging)
                        .build();
            } else {
                okHttpClient = builder.build();
            }
        }

    }


    public <T> T getTApi(Class<T> serviceClass) {
        return getRetrofit().create(serviceClass);
    }

    /**
     * 子类 单例模式下 创建ServerApi 对象时，需要添加该判断，用于动态切换 baseUrl
     * @return
     */
    public boolean isReseted() {
        if (TextUtils.isEmpty(mBaseUrl) || build == null) {
            return true;
        }
        return false;
    }


    public static void resetBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        build = null;
    }


    @NonNull
    private static Retrofit getRetrofit() {
        if (build == null) {
            build = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();

        }
        return build;


    }
}