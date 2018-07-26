package com.wangke.core.utils;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

public class ZGPermissionUtil {


    public static boolean hasAlwaysDeniedPermission(Fragment fragment, List<String> deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(Context context, List<String> deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions);
    }


    public static boolean hasAlwaysDeniedPermission(Fragment fragment, String... deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(android.app.Fragment fragment, String... deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(fragment, deniedPermissions);
    }

    public static boolean hasAlwaysDeniedPermission(Context context, String... deniedPermissions) {
        return AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions);
    }


    public static boolean hasPermissions(Fragment fragment, String... permissions) {
        return hasPermissions(fragment.getContext(), permissions);
    }

    public static boolean hasPermissions(android.app.Fragment fragment, String... permissions) {
        return hasPermissions(fragment.getActivity(), permissions);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        return AndPermission.hasPermissions(context, permissions);
    }

    public static boolean hasPermissions(Fragment fragment, String[]... permissions) {
        return hasPermissions(fragment.getContext(), permissions);
    }

    public static boolean hasPermissions(android.app.Fragment fragment, String[]... permissions) {
        return hasPermissions(fragment.getActivity(), permissions);
    }

    public static boolean hasPermissions(Context context, String[]... permissions) {
        return AndPermission.hasPermissions(context, permissions);
    }


    public static void requestPermission(Context context, final OnPermissionCallback callback, String... permissions) {

        AndPermission.with(context)
                .runtime()
                .permission(
                        permissions
                )
                .rationale(new Rationale<List<String>>() {
                    @Override
                    public void showRationale(Context context, List<String> data, RequestExecutor executor) {
                        LogUtil.e("rationale", data.toString());
                        executor.execute();
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        callback.onGranted(data);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        callback.onDenied(data);
                    }
                })
                .start();
    }

    public static void requestPermission(Context context, final OnPermissionCallback callback, String[]... permissions) {
        AndPermission.with(context)
                .runtime()
                .permission(
                        permissions
                )
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        callback.onGranted(data);

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        callback.onDenied(data);
                    }
                })
                .start();
    }

    public interface OnPermissionCallback {
        void onGranted(List<String> data);

        void onDenied(List<String> data);
    }

}
