package aacom.wangke.wkappw.http;

import com.wangke.core.retrofit.BaseEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApi {


    @FormUrlEncoded
    @POST("mainapp/v1/user/login")
    Observable<BaseEntity> login2(@FieldMap() Map<String, String> map);

    @FormUrlEncoded
    @POST("mainapp/v1/user/login")
    Observable<BaseEntity> login(@FieldMap() Map<String, String> map);

    @GET("https://www.zhiguohulian.com/404")
    Observable<BaseEntity> login404();

    @FormUrlEncoded
    @POST("mainapp/v1/user/login")
    Call<BaseEntity> loginCallback(@FieldMap() Map<String, String> map);

}
