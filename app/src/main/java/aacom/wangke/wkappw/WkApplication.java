package aacom.wangke.wkappw;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.wangke.core.WkCoreLib;
import com.wangke.core.retrofit.BaseRetrofit;
import com.wangke.core.utils.LogUtil;

import aacom.wangke.wkappw.http.UrlConstants;

/**
 * Created by wk on 2017/11/1.
 */

public class WkApplication extends MultiDexApplication {

    public static Context context;
    private static WkApplication application;

    public static synchronized final WkApplication getInstance() {
        if (application == null) {
            application = new WkApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        application = this;
        WkCoreLib.init(this, BuildConfig.DEBUG);
        LogUtil.setDebug(BuildConfig.DEBUG);
        BaseRetrofit.init(UrlConstants.PRE_PUBLISH_ENV, 6000l, BuildConfig.DEBUG);
    }


}
