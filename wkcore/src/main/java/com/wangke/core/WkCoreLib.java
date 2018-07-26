package com.wangke.core;

import android.content.Context;

public class WkCoreLib {

    private static Context mContext;

    public static void init(Context context,boolean isDebug){
        mContext = context;
//        LogUtil.setDebug(isDebug);
    }

    public static Context getInstance(){
        if(mContext == null){
            throw new RuntimeException("工具库未初始化");
        }
        return mContext;
    }


}
