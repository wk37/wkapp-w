package aacom.wangke.wkappw.http;

import com.wangke.core.retrofit.BaseRetrofit;



public class AppRetrofit extends BaseRetrofit {

    private static ServerApi serverApi;
    private static AppRetrofit retrofitUtil;


    public static AppRetrofit getInstance() {
        if (retrofitUtil == null) {
            synchronized (AppRetrofit.class) {
                if (retrofitUtil == null) {
                    retrofitUtil = new AppRetrofit();
                }
            }
        }
        return retrofitUtil;
    }


    public static ServerApi getAppApi() {
        if (serverApi == null || getInstance().isReseted()) {
            serverApi = getInstance().getTApi(ServerApi.class);
        }
        return serverApi;
    }



}
