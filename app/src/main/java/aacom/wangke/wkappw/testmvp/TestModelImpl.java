package aacom.wangke.wkappw.testmvp;


import com.wangke.core.retrofit.BaseCallback;
import com.wangke.core.retrofit.BaseEntity;
import com.wangke.core.retrofit.BaseObserver;
import com.wangke.core.retrofit.RxSchedulers;

import java.util.HashMap;
import java.util.Map;

import aacom.wangke.wkappw.http.AppRetrofit;

public class TestModelImpl implements TestContract.TestModel {


    @Override
    public void getDataFromSD(final TestContract.Callback callback) {

        Map<String, String> map = new HashMap<>();


        AppRetrofit.getAppApi().loginCallback(map).enqueue(new BaseCallback() {


            @Override
            public void onSuccess(int code, Object data) {
                callback.onResult(data.toString());

            }

            @Override
            public void onError(int code, String msg) {
                callback.onResult(msg.toString());

            }
        });


    }

    @Override
    public void getDataFromNet(final TestContract.Callback callback) {

        testlogin(callback);

    }

    private void testlogin(final TestContract.Callback callback) {
        Map<String, String> map = new HashMap<>();

        AppRetrofit.getAppApi().login(map)
                .compose(RxSchedulers.<BaseEntity>ioMain())
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(int code, Object data) {
                        callback.onResult(data.toString());

                    }

                    @Override
                    public void onError(int code, String msg) {
                        callback.onResult(msg.toString());

                    }
                });
    }
}
