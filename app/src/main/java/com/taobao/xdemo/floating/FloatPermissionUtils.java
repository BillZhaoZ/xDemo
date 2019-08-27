package com.taobao.xdemo.floating;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author bill
 * @Date on 2019-08-27
 * @Desc: 权限管理类
 */
public class FloatPermissionUtils {

    /**
     * 获取悬浮窗权限状态
     *
     * @param context
     * @return 1或其他是没有打开，0是打开，该状态的定义和{@link android.app.AppOpsManager#MODE_ALLOWED}，MODE_IGNORED等值差不多，自行查阅源码
     */
    public static int getFloatPermissionStatus(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("content://com.iqoo.secure.provider.secureprovider/allowfloatwindowapp");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.getColumnNames();
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentlmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return getFloatPermissionStatus2(context);
            }

        } else {
            return getFloatPermissionStatus2(context);
        }
    }

    /**
     * vivo比较新的系统获取方法
     *
     * @param context
     * @return
     */
    private static int getFloatPermissionStatus2(Context context) {
        String packageName = context.getPackageName();
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/float_window_apps");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = context
                .getContentResolver()
                .query(uri2, null, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int currentmode = cursor.getInt(cursor.getColumnIndex("currentmode"));
                cursor.close();
                return currentmode;
            } else {
                cursor.close();
                return 1;
            }
        }
        return 1;
    }

}
