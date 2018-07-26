package com.wangke.core.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by XHSJ on 2017/11/21.
 */

public class LogUtil {

    public static boolean isLog = false;
    private static boolean mIsWrite = false;
    private static String mLogDir = "";
    private static DateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss:SSS");
    private static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH");

    private static LogUtil sLogUtil = new LogUtil();

    private LogUtil(){

    }

    public static LogUtil getInstance(){
        return sLogUtil;
    }

    public static void setDebug(boolean debug){
        isLog = debug;
    }

    public LogUtil setIsWrite(boolean isWrite) {
        mIsWrite = isWrite;
        return this;
    }

    public LogUtil setLogDir(String logDir){
        mLogDir = logDir;
        return this;
    }

    public static void d(String TAG, String msg){
        if(isLog){
            if(msg!=null){
                Log.d(TAG," <=== "+msg+" ===> ");
            }
        }
    }
    public static void d( String msg){
        if(isLog){
            if(msg!=null){
                Log.d(""," <=== "+msg+" ===> ");
            }
        }
    }

    public static void e(String TAG,String msg){
        if(isLog){
            if(msg!=null){
                Log.e(TAG," <--- "+msg+" ---> ");
            }
        }

    }
    public static void i(String TAG,String msg){
        if(isLog){
            if(msg!=null){
                Log.e(TAG," <--- "+msg+" ---> ");
            }
        }

    }
    public static void e(String msg){
        if(isLog){
            if(msg!=null){
                Log.e(""," <--- "+msg+" ---> ");
            }
        }

    }

//    public static void write(String tag,String msg){
//        d(tag,msg);
//        if(mIsWrite && !TextUtils.isEmpty(mLogDir)){
//            String time = timeFormatter.format(new Date());
//            String date = dateFormatter.format(new Date());
//            while(tag.length()<32){
//                tag += " ";
//            }
//            String content = time+"    "+tag+"\t"+msg+"\n";
//            String name = "zg"+"_"+date+".log";
//            FileUtil.appendContent(mLogDir,name,content);
//        }
//    }

}
