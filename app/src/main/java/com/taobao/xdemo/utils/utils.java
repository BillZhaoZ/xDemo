package com.taobao.xdemo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.taobao.xdemo.MainActivity;
import com.taobao.xdemo.R;
import com.taobao.xdemo.TimeActivity;
import com.taobao.xdemo.smartlink.SnartLinkActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * @author bill
 * @Date on 2019-06-11
 * @Desc:
 */
public class utils {

    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private static final String mPinShortcutId = "hankinhui_shortcut";
    private android.widget.LinearLayout viewtopmsgparent;

    // https://www.cnblogs.com/mengdd/p/3837592.html

    public static void addShortcut(Context context, String labelName, String labelId, int resId, String url) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int request = context.checkSelfPermission(ACTION_ADD_SHORTCUT);

            if (request != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)context, new String[] {ACTION_ADD_SHORTCUT}, 1222);
                Log.e("lmtest", "没有权限");
            }

            ShortcutManager shortcutManager = (ShortcutManager)context.getSystemService(
                Context.SHORTCUT_SERVICE);//获取shortcutManager
            //如果默认桌面支持requestPinShortcut（ShortcutInfo，IntentSender）方法，则返回TRUE。

          /*  List<ShortcutInfo> infos = shortcutManager.getPinnedShortcuts();
            for (int i = 0; i < infos.size(); i++) {
                ShortcutInfo info = infos.get(i);
                if (info.getId().equals(mPinShortcutId)) {
                    if (info.isEnabled()) {//当pinned shortcut为disable时，重复创建会导致应用奔溃
                        break;
                    } else {
                        return;
                    }
                }
            }*/

            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                Uri uri = Uri.parse(url);
                Intent shortCutIntent = new Intent(Intent.ACTION_VIEW, uri);//快捷方式启动页面
                shortCutIntent.setAction(Intent.ACTION_VIEW);

                //快捷方式创建相关信息。图标名字 labelId
                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, labelId)
                    .setIcon(Icon.createWithResource(context, resId))
                    .setShortLabel(labelName)
                    .setIntent(shortCutIntent)
                    .build();
                //创建快捷方式时候回调
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new
                    Intent(context, SnartLinkActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                shortcutManager.requestPinShortcut(shortcutInfo, pendingIntent.getIntentSender());

            } else {
                Toast.makeText(context, "设备不支持在桌面创建快捷图标！!!!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

            // 不允许重复创建
            addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
            // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
            // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
            // 屏幕上没有空间时会提示
            // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

            // 名字
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, labelName);

            // 图标
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, resId));

            // 设置关联程序
            Uri uri = Uri.parse(url);
            Intent launcherIntent = new Intent(Intent.ACTION_VIEW, uri);

            addShortcutIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

            // 发送广播
            context.sendBroadcast(addShortcutIntent);
        }

        Toast.makeText(context, "创建快捷方式成功", Toast.LENGTH_SHORT).show();
    }

    private void removeShortcut(Context context, String name) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);

        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 设置关联程序
        Intent launcherIntent = new Intent(context, MainActivity.class)
            .setAction(Intent.ACTION_MAIN);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        context.sendBroadcast(intent);
    }

    /**
     * 创建View并启动动画
     */
    public static View getView(final Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_top_msg, null);

        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 900, 0, 0);
        view.setLayoutParams(layoutParams);

        view.setOnTouchListener(new View.OnTouchListener() {
            private int lastX = 0;
            private int lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int)event.getRawX();
                int y = (int)event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 记录触摸点坐标
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 计算偏移量
                        int offsetX = x - lastX;
                        int offsetY = y - lastY;

                        int left = view.getLeft() + offsetX;
                        int top = view.getTop() + offsetY;
                        int right = view.getRight() + offsetX;
                        int bottom = view.getBottom() + offsetY;

                        if (left > 0) {
                            left = 0;
                        }

                        // 在当前left、top、right、bottom的基础上加上偏移量
                        view.layout(left, top, right, bottom);

                        //重新设置初始化坐标
                        lastX = x;
                        lastY = y;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (view.getLeft() < -(view.getWidth() / 4)) {
                            view.setVisibility(View.GONE);
                        }
                        break;
                }

                return true;
            }
        });

        return view;
    }

    ////////////////// //////////////////   流量数据统计   ////////////////// ////////////////// /////

    /**
     * 流量枚举类型
     */
    public enum FlowType {
        //分享
        SHARE(1, "afc_share"),

        //消息
        MESSAGE(2, "afc_message"),

        //外链
        LINK(3, "afc_link"),

        //外链
        LAUNCH(4, "afc_launch");

        private int code;
        private String descs;

        FlowType(int code, String descs) {
            this.code = code;
            this.descs = descs;
        }

        public static String getDesc(int value) {
            for (FlowType flowType : FlowType.values()) {
                if (flowType.code == value) {
                    return flowType.descs;
                }
            }
            return null;
        }

    }

    /**
     * 改方法很重要！！！ Bi在用  只要涉及到改动，请完全回归！！！
     *
     * @param flowType   流量类型，枚举，目前包括外链拉端，分享回流，push启动（必选）
     * @param landingUrl 进端URL
     * @param extraMap   备用字典(会透传全链路)（必选）
     * @return https://yuque.antfin-inc.com/lzksb7/suy1p9/mmwdoz
     */
    public static String handleFlowParams(FlowType flowType, String landingUrl, Map<String, String> extraMap) {
        StringBuffer afcId = new StringBuffer();

        // 生成afc_id
        String afcIdPostFix = UUID.randomUUID().toString() + "_" + getCurrentTime();

        if (extraMap == null) {
            extraMap = new HashMap<>();
        }
        extraMap.put("url", landingUrl);

        try {
            if (flowType == FlowType.SHARE) {
                // 分享 extraMap传入原始URL  需要解析ut_sk   ut_sk=1.[utdid][appKey]tiemstamp.[渠道].[bzid]
                // ut_sk=1.utdid_appKey_1556072379528.TaoPassword-Outside.taoketop
                String appKey = "unknown";
                String busniess = "unknown";

                Uri data = Uri.parse(landingUrl);

                if (data != null) {
                    String ut_sk = data.getQueryParameter("ut_sk");

                    if (!TextUtils.isEmpty(ut_sk)) {
                        String[] split = ut_sk.split("\\.");
                        if (split.length > 3) {
                            String channel = !TextUtils.isEmpty(split[2]) ? split[2] : "unknown";
                            String bizId = !TextUtils.isEmpty(split[3]) ? split[3] : "unknown";

                            String tempData = split[1];
                            String[] partsData = tempData.split("_");
                            appKey = !TextUtils.isEmpty(partsData[1]) ? partsData[1] : "unknown";
                            busniess = channel + "_" + bizId;   //channel_bizid
                        }
                    }
                }

                afcId = afcId.append(FlowType.SHARE.descs).append("^").append(appKey).append("^")
                    .append(busniess).append("^").append(afcIdPostFix);

            } else if (flowType == FlowType.MESSAGE) {
                // 消息 传递参数  bizType,messageId,messageType,senderId,receiverId,sendTime,shwoTime,pushId,folderMsgs
                // 需要消息把目前打在arg2里的参数，“是否是应用内push”传过来  打在arg3里的参数，“跳转URL”传过来
                String messageId = extraMap.get("messageId");

                afcId = afcId.append(FlowType.MESSAGE.descs).append("^").append(messageId).append("^")
                    .append(messageId).append("^").append(afcIdPostFix);

            } else if (flowType == FlowType.LINK) {
                // 海关外链
                String packageName = extraMap.get("packageName");
                String source = !TextUtils.isEmpty(packageName) ? packageName : "unknown";
                String bc_fl_src = extraMap.get("bc_fl_src");
                String busniess = !TextUtils.isEmpty(bc_fl_src) ? bc_fl_src : "nbc";
                String tag = TextUtils.equals("true", extraMap.get("is_link")) ? FlowType.LINK.descs : "afc_unlink";

                // 对登录授权特殊处理
                if (isLoginLink(landingUrl)) {
                    busniess = "is_auth";
                }

                afcId = afcId.append(tag).append("^").append(source).append("^").append(busniess)
                    .append("^").append(afcIdPostFix);

            } else if (flowType == FlowType.LAUNCH) {
                // 正常冷启
                afcId = afcId.append(FlowType.LAUNCH.descs).append("^").append("com.taobao.taobao").append("^")
                    .append("1012_Initiactive").append("^").append(afcIdPostFix);
            }

        } catch (Exception e) {
            FlowCustomLog.d(LOG_TAG, "TFCCommonUtils === flowParamsHandle: 异常:" + e.toString());
        }

        // 全局埋点
        //        UTAnalytics.getInstance().getDefaultTracker().setGlobalProperty("_afc_id", afcId.toString());
        // 1013埋点
        //   UserTrackUtils.sendCustomHit(UserTrackUtils.EVENT_ID_1013, "afc_flow_track", afcId.toString(), "",
        //   extraMap);

        FlowCustomLog.d(LOG_TAG, "TFCCommonUtils === flowParamsHandle: afcId = " + afcId.toString());
        return afcId.toString();
    }

    /**
     * 是否是登录授权唤端
     * tbopen://m.taobao.com/sso
     * tbopen://m.taobao.com/getway/oauth
     *
     * @param url
     * @return
     */
    public static boolean isLoginLink(String url) {
        if (url != null) {
            if (url.contains("tbopen://m.taobao.com/sso") || url.contains("tbopen://m.taobao.com/getway/oauth")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isHome(Context context) {
        try {
            ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);

            //            return getHomes(context).contains(rti.get(0).topActivity.getPackageName());

            return getHomes(context).contains(getTopApp(context));

        } catch (Throwable e) {
            Log.d(LOG_TAG, "LinkUtils === isHome === 判断异常" + e);
        }

        return false;
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

    public static String getTopApp(Context context) {
        //android5.0 above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            UsageStatsManager m = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (null != m) {
                long now = System.currentTimeMillis();
                //obtain recent in 1 hour app status
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000 * 60, now);
                String topActivity = "";
                //obtain recent run app，maybe running app
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();

                   /* UsageStats recentStats = null;
                    for (UsageStats usageStats : stats) {
                        if (recentStats == null
                                || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                            recentStats = usageStats;
                        }
                    }
                    String packname = recentStats.getPackageName();

                    return packname;*/
                }

                return topActivity;
            }
        } else {
            //android5.0 below
            ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
            return rti.get(0).topActivity.getPackageName();
        }
        return null;
    }

    public static void checkUsageStateAccessPermission(Context context) {
        if (!checkAppUsagePermission(context)) {
            requestAppUsagePermission(context);
        }
    }

    public static boolean checkAppUsagePermission(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager == null) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        // try to get app usage state in last 1 min
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
            currentTime - 60 * 1000, currentTime);
        if (stats.size() == 0) {
            return false;
        }

        return true;
    }

    public static void requestAppUsagePermission(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i("haha", "Start usage access settings activity fail!");
        }
    }

    @RequiresApi(api = VERSION_CODES.N_MR1)
    public static void setupShortcuts(Context context) {

        ShortcutManager mShortcutManager = context.getSystemService(ShortcutManager.class);
        List<ShortcutInfo> infos = new ArrayList<>();

        for (int i = 0; i < mShortcutManager.getMaxShortcutCountPerActivity(); i++) {

            Intent intent = new Intent(context, TimeActivity.class);
            intent.setAction(Intent.ACTION_VIEW);

            ShortcutInfo info = new ShortcutInfo.Builder(context, "id" + i)
                .setShortLabel("hahahahah")
                .setLongLabel("联系人:22222")
                .setIcon(Icon.createWithResource(context, R.drawable.icon))
                .setIntent(intent)
                .build();
            infos.add(info);
        }

        mShortcutManager.setDynamicShortcuts(infos);
    }
}
