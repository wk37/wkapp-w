package com.wangke.core.utils;


import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by itgoyo on 2017/2/23.
 * <p>
 * 更新时间 2017/2/23
 * 更新描述 ${TODO}
 *
 *          LogToFileUtils.init(AcsApplication.getContext());
 *          String logStr = "";
 *          LogToFileUtils.write(logStr, time);
 */

public class LogToFileUtils {

    private static    String LOG_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Log/";

    /**
     * 上下文对象
     */
    private static Context mContext;
    /**
     * FileLogUtils类的实例
     */
    private static LogToFileUtils instance;
    /**
     * 用于保存日志的文件
     */
    private static File logFile;
    /**
     * 日志中的时间显示格式
     */
    private static SimpleDateFormat logSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat logSDF2 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat logSDF3 = new SimpleDateFormat("HH:mm:ss");
    /**
     * 日志的最大占用空间 - 单位：字节
     * <p>
     * 注意：为了性能，没有每次写入日志时判断，故日志在写入第二次初始化之前，不会受此变量限制，所以，请注意日志工具类的初始化时间
     * <p>
     * 为了衔接上文，日志超出设定大小后不会被直接删除，而是存储一个副本，所以实际占用空间是两份日志大小
     * <p>
     * 除了第一次超出大小后存为副本外，第二次及以后再次超出大小，则会覆盖副本文件，所以日志文件最多也只有两份
     * <p>
     * 默认10M
     */
    private static final int LOG_MAX_SIZE = 10 * 1024 * 1024;
    /**
     * 以调用者的类名作为TAG
     */
    private static String tag="LogToFileUtils";
    private static String myFlieName;

    private static final String MY_TAG = "LogToFileUtils";

    /**
     * 初始化日志库
     *
     * @param context
     */
/*    public static void init(Context context) {
        Log.i(MY_TAG, "init ...");
        if (null == mContext || null == instance || null == logFile || !logFile.exists()) {
            mContext = context;
            instance = new LogToFileUtils();
            logFile = getLogFile();
            Log.i(MY_TAG, "LogFilePath is: " + logFile.getPath());
            // 获取当前日志文件大小
            long logFileSize = getFileSize(logFile);
            Log.d(MY_TAG, "Log max size is: " + Formatter.formatFileSize(context, LOG_MAX_SIZE));
            Log.i(MY_TAG, "log now size is: " + Formatter.formatFileSize(context, logFileSize));
            // 若日志文件超出了预设大小，则重置日志文件
            if (LOG_MAX_SIZE < logFileSize) {
                resetLogFile();
            }
        } else {
            Log.i(MY_TAG, "LogToFileUtils has been init ...");
        }
    }*/
    public static void init(Context context) {
        Log.i(MY_TAG, "init ...");
        if (null == mContext || null == instance) {
            mContext = context;
            instance = new LogToFileUtils();
        } else {
            Log.i(MY_TAG, "LogToFileUtils has been init ...");
        }

        String format = logSDF2.format(new Date());
        Log.i(MY_TAG, "old FlieName: "+myFlieName+"     new  FlieName: "+format);

        if (TextUtils.isEmpty(myFlieName)) {
            myFlieName = format;
        } else if ( !TextUtils.equals(format, myFlieName)) {
            myFlieName = format;
            logFile = null;
        }

        if (null == logFile || !logFile.exists()) {
            logFile = getLogFile(myFlieName);
            Log.i(MY_TAG, "LogFilePath is: " + logFile.getPath());
        }

    }

    /**
     * 写入日志文件的数据
     *
     * @param obj 需要写入的数据
     */
    public static void write(Object obj, String time) {

        // 判断是否初始化或者初始化是否成功
        if (null == mContext || null == instance || null == logFile || !logFile.exists()) {
            Log.e(MY_TAG, "Initialization failure !!!");
            return;
        }


        try {
            String logStr=getFunctionInfo_Time(time) +","+obj.toString();

            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(logStr);
            bw.write("\r\n");
            bw.flush();
        } catch (Exception e) {
            Log.e(tag, "Write failure !!! " + e.toString());
        }
    }

    /**
     * 重置日志文件
     * <p>
     * 若日志文件超过一定大小，则把日志改名为lastLog.txt，然后新日志继续写入日志文件
     * <p>
     * 每次仅保存一个上一份日志，日志文件最多有两份
     * <p/>
     */
    private static void resetLogFile() {
        Log.i(MY_TAG, "Reset Log File ... ");
        // 创建lastLog.txt，若存在则删除
        File lastLogFile = new File(logFile.getParent() + "/lastLog.txt");
        if (lastLogFile.exists()) {
            lastLogFile.delete();
        }
        // 将日志文件重命名为 lastLog.txt
        logFile.renameTo(lastLogFile);
        // 新建日志文件
        try {
            logFile.createNewFile();
        } catch (Exception e) {
            Log.e(MY_TAG, "Create log file failure !!! " + e.toString());
        }
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                Log.e(MY_TAG, e.toString());
            }
        }
        return size;
    }

    /**
     * 获取APP日志文件
     *
     * @return APP日志文件
     */
    private static File getLogFile(String fileName) {
        File file;
            // 有SD卡则使用SD - PS:没SD卡但是有外部存储器，会使用外部存储器
            // SD\Android\data\包名\files\Log\logs.txt
            file = new File(LOG_DIR);
        // 若目录不存在则创建目录
        if (!file.exists()) {
            file.mkdir();
        }
        File logFile = new File(file.getPath() + "/"+ Build.SERIAL +"_" + fileName + "_ad.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                Log.e(MY_TAG, "Create log file failure !!! " + e.toString());
            }
        }
        return logFile;
    }

    /**
     * 获取当前函数的信息
     *
     * @return 当前函数的信息
     */
    private static String getFunctionInfo() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(instance.getClass().getName())) {
                continue;
            }
            tag = st.getFileName();
            return "[" + logSDF.format(new Date()) + " " + st.getClassName() + " " + st
                    .getMethodName() + " Line:" + st.getLineNumber() + "]";
        }
        return null;
    }

    private static String getFunctionInfo(String time) {
        return "[" + logSDF.format(new Date(Long.parseLong(time))) + "]";

    }

    private static String getFunctionInfo_Time(String time) {
        return logSDF3.format(new Date(Long.parseLong(time))) ;

    }

}