package com.wangke.core.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/10
 *    desc   : 工具类: Intent 意图相关
 *
 *             注: 部分 API 直接参考或使用 https://github.com/Blankj/AndroidUtilCode 中 IntentUtils 类里面的方法
 * </pre>
 */
public class IntentUtils {

    private IntentUtils() {
    }

    //================================================ Intent 意图的获取 ================================================//

    /**
     * 获取跳转「设置界面」的意图
     *
     * @return 意图
     */
    public static Intent getSettingIntent() {
        return new Intent(Settings.ACTION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳转「应用详情」的意图
     *
     * @param packageName 应用包名
     * @return 意图
     */
    public static Intent getAppDetailsSettingsIntent(String packageName) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + packageName));
    }

    /**
     * 获取跳转「应用列表」的意图
     *
     * @return 意图
     */
    public static Intent getAppsIntent() {
        return new Intent(Settings.ACTION_APPLICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳转「 Wifi 设置」的意图
     *
     * @return 意图
     */
    public static Intent getWifiSettingIntent() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳转「网络设置」的意图
     *
     * @return 意图
     */
    public static Intent getWirelessSettingIntent() {
        return new Intent(Settings.ACTION_WIRELESS_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取跳转「无障碍服务设置」的意图
     *
     * @return 意图
     */
    public static Intent getAccessibilitySettingIntent() {
        return new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //================================================ Intent 意图的跳转 ================================================//


    /**
     * 跳转:「设置」界面
     */
    public static void gotoSetting(Context context) {
        context.startActivity(getSettingIntent());
    }

    /**
     * 跳转:「应用详情」界面
     *
     * @param packageName 应用包名
     */
    public static void gotoAppDetailSetting(Context context, String packageName) {
        context.startActivity(getAppDetailsSettingsIntent(packageName));
    }

    /**
     * 跳转:「应用程序列表」界面
     */
    public static void gotoAppsSetting(Activity activity) {
        activity.startActivity(getAppsIntent());
    }

    /**
     * 跳转:「Wifi列表」设置
     */
    public static void gotoWifiSetting(Activity activity) {
        activity.startActivity(getWifiSettingIntent());
    }


    /**
     * 跳转:「飞行模式，无线网和网络设置」界面
     */
    public static void gotoWirelessSetting(Activity activity) {
        activity.startActivity(getWirelessSettingIntent());
    }

    /**
     * 跳转:「无障碍设置」界面
     */
    public static void gotoAccessibilitySetting(Activity activity) {
        activity.startActivity(getAccessibilitySettingIntent());
    }


    /**
     * 跳转: 「权限设置」界面
     * <p>
     * 根据各大厂商的不同定制而跳转至其权限设置
     * 目前已测试成功机型: 小米V7V8V9, 华为, 三星, 锤子, 魅族; 测试失败: OPPO
     *
     * @return 成功跳转权限设置, 返回 true; 没有适配该厂商或不能跳转, 则自动默认跳转设置界面, 并返回 false
     */
    public static void gotoPermissionSetting(Activity activity, int requestCode) {
        boolean success = true;
        OSUtils.ROM romType = OSUtils.getRomType();
        Intent intent = getPermissionIntent(activity, romType);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            // 跳转失败, 前往普通设置界面
            IntentUtils.gotoSetting(activity);
            success = false;
            Log.d("", "无法跳转权限界面, 开始跳转普通设置界面");
        }
    }

    public static void gotoNotificationSetting(Context context) {
        Intent intent = new Intent();

        OSUtils.ROM romType = OSUtils.getRomType();
        String packageName = context.getPackageName();
        switch (romType) {
            case MIUI:
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationFilterActivity");
                Bundle bundle = new Bundle();
                bundle.putString("appName", context.getResources().getString(context.getApplicationInfo().labelRes));
                bundle.putString("packageName", packageName);
                bundle.putString(":android:show_fragment", "NotificationAccessSettings");
                intent.putExtras(bundle);
                intent.setComponent(cn);
                break;
            case Sony: // 索尼
                intent =   getAppDetailsSettingsIntent(packageName);
                break;
            default:
                intent = getNotificationIntent(context, romType);
                break;
        }

        boolean flag1=true;
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            flag1=false;
        }
        if (!flag1) {
            try {
                IntentUtils.gotoAppDetailSetting(context, packageName);
            } catch (Exception e) {
                e.printStackTrace();
                // 跳转失败, 前往普通设置界面
                IntentUtils.gotoSetting(context);
                Log.d("", "无法跳转权限界面, 开始跳转普通设置界面");
            }
        }

    }

    @NonNull
    private static Intent getPermissionIntent(Context context, OSUtils.ROM romType) {
        boolean success;
        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String packageName = context.getPackageName();
        switch (romType) {
            case EMUI: // 华为
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
                break;
            case Flyme: // 魅族
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra("packageName", packageName);
                break;
            case MIUI: // 小米
                String rom = getMiuiVersion();
                if ("V6".equals(rom) || "V7".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else if ("V8".equals(rom) || "V9".equals(rom)) {
                    intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", packageName);
                } else {
//                    intent = getAppDetailsSettingsIntent(packageName);
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", packageName, null));
                }
                break;
            case EUI: // 乐视
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps"));
                break;
            case LG: // LG
                intent.setAction("android.intent.action.MAIN");
                intent.putExtra("packageName", packageName);
                ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                intent.setComponent(comp);
                break;
            case ColorOS: // OPPO
/*                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionManagerActivity"));
                break;*/
            case SamSung: // 三星
            case SmartisanOS: // 锤子
            case Sony: // 索尼
            default:
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", packageName, null));
                /*intent.setAction(Settings.ACTION_SETTINGS);
                Log.i("没有适配该机型, 跳转普通设置界面");*/
                success = false;
                break;
        }
        return intent;
    }

    private static Intent getNotificationIntent(Context context, OSUtils.ROM romType) {
        boolean success;
        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String packageName = context.getPackageName();
        switch (romType) {
            case EMUI: // 华为
                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationSettingsActivity"));
                break;

            case LG: // LG
                intent.setAction("android.intent.action.MAIN");
                intent.putExtra("packageName", packageName);
                ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                intent.setComponent(comp);
                break;
            case ColorOS: // OPPO
/*                intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionManagerActivity"));
                break;*/
            case SmartisanOS: // 锤子
             /*   intent.putExtra("packageName", packageName);
                intent.setComponent(new ComponentName("com.smartisanos.security", ".PackageDetail"));

                break;*/
            case SamSung: // 三星

            case Flyme: // 魅族
            case EUI: // 乐视
            default:
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package",packageName);
                intent.putExtra("app_uid", context.getApplicationInfo().uid);
                break;
        }
        return intent;
    }


    /**
     * 获取 MIUI 版本号
     */
    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("MiuiVersion = ", line);
        return line;
    }
}
