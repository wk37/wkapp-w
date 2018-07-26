package com.wangke.core.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.wangke.core.WkCoreLib;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

/**
 * Author：billXiang
 * Date  : 2017/5/19下午5:22
 * Desc  : 项目工具类
 */

public class AppUtils {

    /**
     * 每4位添加一个空格
     *
     * @param content
     * @return
     */
    public static String addSpeaceByCredit(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        content = content.replaceAll(" ", "");
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        StringBuilder newString = new StringBuilder();
        for (int i = 1; i <= content.length(); i++) {
            if (i % 4 == 0 && i != content.length()) {
                newString.append(content.charAt(i - 1) + " ");
            } else {
                newString.append(content.charAt(i - 1));
            }
        }
        return newString.toString();
    }

//    /**
//     * 网络是否可用
//     *
//     * @return
//     */
//    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE})
//    public static boolean networkAvailable() {
//        NetworkState state = getNetworkState();
//        return NetworkState.UNAVAILABLE != state && NetworkState.NONE != state;
//    }


    /**
     * 网络是否可用
     *
     * @return
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE})
    public static boolean networkAvailable() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }


    /**
     * 获取活动网络信息
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return NetworkInfo
     */

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE})
    private static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) WkCoreLib.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 获取网络状态
     *
     * @return
     */
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE})
    public static NetworkState getNetworkState() {
        ConnectivityManager cm = (ConnectivityManager) WkCoreLib.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info) {
                if (info.isAvailable()) {
                    switch (info.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:
                        case ConnectivityManager.TYPE_WIFI:
                        case ConnectivityManager.TYPE_ETHERNET:
                        case ConnectivityManager.TYPE_MOBILE_DUN:
                        case ConnectivityManager.TYPE_VPN:
                        case ConnectivityManager.TYPE_WIMAX:
                        case ConnectivityManager.TYPE_DUMMY:
                        case ConnectivityManager.TYPE_BLUETOOTH:
                        case ConnectivityManager.TYPE_MOBILE_HIPRI:
                        case ConnectivityManager.TYPE_MOBILE_MMS:
                        case ConnectivityManager.TYPE_MOBILE_SUPL:
                            return NetworkState.AVAILABLE;
                    }
                } else {
                    return NetworkState.UNAVAILABLE;
                }
            }
        }
        return NetworkState.NONE;
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE})
    public static int getConnectedNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) WkCoreLib.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info) {
                if (info.isAvailable()) {
                    switch (info.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:
                            return ConnectivityManager.TYPE_MOBILE;
                        case ConnectivityManager.TYPE_WIFI:
                            return ConnectivityManager.TYPE_WIFI;
                        case ConnectivityManager.TYPE_ETHERNET:
                            return ConnectivityManager.TYPE_ETHERNET;
                    }
                }
            }
        }
        return 9999;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE})
    public static boolean getNetworkConnectStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WkCoreLib.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取IP地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @param useIPv4 是否用IPv4
     * @return IP地址
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机卡的运营商
     *
     * @return
     */
    public static String getNetworkOperatorName() {
        TelephonyManager telephonyManager = (TelephonyManager) WkCoreLib.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkOperatorName() != null) {
            return telephonyManager.getNetworkOperatorName();
        }
        return "未获取到";
    }

    /**
     * 获取收据网络类型
     *
     * @return
     */
    public static String getNetworkType() {
        TelephonyManager telephonyManager = (TelephonyManager) WkCoreLib.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        //获取手机网络类型
        // TODO Auto-generated method stub
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "EHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO_0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO_A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO_B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPAP";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "IDEN";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * 获取App版本码
     *
     * @return App版本码
     */
    public static int getAppVersionCode() {
        String packageName = WkCoreLib.getInstance().getPackageName();
        if (isSpace(packageName)) return -1;
        try {
            PackageManager pm = WkCoreLib.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 获取 App 版本号
     *
     * @return App 版本号
     */
    public static String getAppVersionName() {
        String packageName = WkCoreLib.getInstance().getPackageName();
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = WkCoreLib.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings(value = "all")
    public static Point getScreenSize(Context context, boolean real) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (real) {
                wm.getDefaultDisplay().getRealSize(point);
            } else {
                wm.getDefaultDisplay().getSize(point);
            }
        } else {
            point.x = wm.getDefaultDisplay().getHeight();
            point.y = wm.getDefaultDisplay().getHeight();
        }
        return point;
    }

    /**
     * 获取手机状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x, height = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return height;
    }

    /**
     * dp 转 px
     *
     * @param dpValue dp 值
     * @return px 值
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

        /**
     * dp 转 px
     *
     * @param dpValue dp 值
     * @return px 值
     */
    public static int dp2px(final float dpValue) {
        final float scale = WkCoreLib.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     *
     * @param pxValue px 值
     * @return dp 值
     */
    public static int px2dp(Context context, final float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * @param context 上下文
     * @param intent  intent携带activity
     * @return boolean true为在最顶层，false为否
     * @Description: TODO 判断activity是否在应用的最顶层
     */
    public static boolean isTop(Context context, Intent intent) {
        if(context == null){
            return false;
        }

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(manager == null){
            return false;
        }
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        if(runningTaskInfos == null || runningTaskInfos.size() == 0){
            return false;
        }

        ActivityManager.RunningTaskInfo firstRunningTask = runningTaskInfos.get(0);
        if(firstRunningTask == null){
            return false;
        }
        ComponentName componentName = firstRunningTask.topActivity;
        if(componentName == null){
            return false;
        }

        String name = componentName.getClassName();
        if(TextUtils.isEmpty(name)){
            return false;
        }
        return name.equals(context.getClass().getName());
    }


    /**
     * 获取系统固件版本号，adb命令获取后取后8位日期作为版本号
     *
     * @return
     */
    public static String getACSFirmware() {
        String systemVer = "";
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd("getprop ro.product.firmware", false);
        if (commandResult != null && !TextUtils.isEmpty(commandResult.successMsg)) {
            systemVer = commandResult.successMsg;
            int length = systemVer.length();
            systemVer = systemVer.substring(length - 8, length);
            LogUtil.e("systemVer", commandResult.successMsg + "   " + systemVer);
        }
        return systemVer;
    }


    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    public static void toggleSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0,0);
        }
    }

    public static String matchTOTP(String password, List<String> phones) {
        if (phones == null || phones.isEmpty()) {
            return "";
        }
        HashSet<String> hashSet = new HashSet<>(phones);
        phones.clear();
        phones.addAll(hashSet);
        for (int i = 0; i < phones.size(); i++) {
            //用本地数据库里的手机号先通过算法算出后四位
            String sqlPass = TOTP.getTotpPsd(phones.get(i));
            //用户输入的密码后四位
            String lastFourPass = password.substring(password.length() - 4, password.length());
            //如果本地的后四位和用户输入的后四位一样就代表查到了
            if (sqlPass.equals(lastFourPass)) {
                return phones.get(i);
            }
        }
        return "";
    }
    public static ArrayList<String> matchTOTPForFace(String password, List<String> phones) {
        if (phones == null || phones.isEmpty()) {
            return null;
        }
        ArrayList<String> list = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>(phones);
        phones.clear();
        phones.addAll(hashSet);
        for (int i = 0; i < phones.size(); i++) {
            //用本地数据库里的手机号先通过算法算出后四位
            String sqlPass = TOTP.getTotpPsd(phones.get(i));
            //用户输入的密码后四位
            String lastFourPass = password.substring(password.length() - 4, password.length());
            //如果本地的后四位和用户输入的后四位一样就代表查到了
            if (sqlPass.equals(lastFourPass)) {
                list.add( phones.get(i));
            }
        }
        return list;
    }
}
