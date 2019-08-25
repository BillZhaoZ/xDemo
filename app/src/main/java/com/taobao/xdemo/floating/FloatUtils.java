package com.taobao.xdemo.floating;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.taobao.xdemo.FlowCustomLog;
import com.taobao.xdemo.notification.NotificationService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * @author bill
 * @Date on 2019-08-20
 * @Desc:
 */
public class FloatUtils {

    /**
     * 打开通知
     *
     * @param context
     */
    public static void openNotification(Context context) {
        //   NotificationUtils.showNotification(getApplicationContext());
        Toast.makeText(context, "打开通知，开启前台服务,快看通知栏", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            context.startForegroundService(new Intent(context, NotificationService.class));
        } else {
            context.startService(new Intent(context, NotificationService.class));
        }
    }

    /**
     * 打开小助手
     *
     * @param context
     */
    public static void startFloatService(Context context) {
        Intent intent = new Intent(context, FloatWindowService.class);
        context.startService(intent);
    }


    // 悬浮窗管理界面
              /*  Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
                ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
                intent.setComponent(comp);
                startActivity(intent);*/

    /**
     * 打开权限设置界面
     */
    public void openSetting(Context context) {
        Toast.makeText(context, "找到手机淘宝，并打开对应开关，有惊喜哦", Toast.LENGTH_LONG).show();

        try {
            Intent localIntent = new Intent(
                    "miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
//            startActivityForResult(localIntent, 11);
//            LogUtil.E("启动小米悬浮窗设置界面");
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent1.setData(uri);
//            startActivityForResult(intent1, 11);
//            LogUtil.E("启动悬浮窗界面");
        }
    }

    private static void applyCommonPermission(Context context) {
        Toast.makeText(context, "找到手机淘宝，并打开对应开关，有惊喜哦", Toast.LENGTH_LONG).show();

        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查是否有悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            FlowCustomLog.d(LOG_TAG, "FloatUtils === checkFloatPermission === 小于14版本，返回true");
            return true;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());

                FlowCustomLog.d(LOG_TAG, "FloatUtils === checkFloatPermission === 小于23版本，返回 = " + declaredField2.getInt(cls));
                return result == declaredField2.getInt(cls);

            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());

//                boolean b = Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
                boolean b = Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED;

                FlowCustomLog.d(LOG_TAG, "FloatUtils === checkFloatPermission === 大于26版本，返回" + b);
                return b;
            } else {

                boolean b = Settings.canDrawOverlays(context);
                FlowCustomLog.d(LOG_TAG, "FloatUtils === checkFloatPermission === 大于23  小于26版本，返回" + b);
                return b;
            }
        }
    }

    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes(context).contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
}
