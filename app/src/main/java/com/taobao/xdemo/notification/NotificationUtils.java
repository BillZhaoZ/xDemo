package com.taobao.xdemo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.taobao.xdemo.R;
import com.taobao.xdemo.floating.TrackUtils;
import com.taobao.xdemo.utils.FlowCustomLog;

import java.util.HashMap;


import static android.content.Context.NOTIFICATION_SERVICE;
import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * @author bill
 * @Date on 2019-08-21
 * @Desc: 通知管理工具类
 */
public class NotificationUtils {

    static final String NOTICE_ID_KEY = "NOTICE_ID";
    static final String ACTION_CLOSE_NOTICE = "cn.campusapp.action.closenotice";
    static final String BROADCAST_FLAG = "flag";
    static final String LANDINGURL = "landingUrl";

    /**
     * 关闭通知
     *
     * @param context
     */
    public static void closeNotification(Context context) {
        Toast.makeText(context, "取消通知,终止前台服务", Toast.LENGTH_SHORT).show();
        context.stopService(new Intent(context, NotificationService.class));
    }

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
     * 绘制通知
     *
     * @param context
     * @return
     */
    public static Notification showNotification(Context context, MessageData messageData) {
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 绘制通知");
        boolean b = NotificationManagerCompat.from(context).areNotificationsEnabled();
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 是否有通知权限：" + b);

        // 通知栏曝光事件
        TrackUtils.sendFloatData(TrackUtils.ARG1_NOTIFICATION_EXPOSE, messageData.ladningUrl, "", new HashMap<String, String>());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.icon_circle);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        // 自定义布局
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_bar);
        remoteViews.setImageViewResource(R.id.iv_setting, R.drawable.delete);
        remoteViews.setImageViewResource(R.id.iv_main_page, R.drawable.jietie);
        remoteViews.setImageViewResource(R.id.iv_main_crazy, R.drawable.hongbao);
        remoteViews.setTextViewText(R.id.tv_title_other, "双十一小助手");

        /**
         * 点击事件处理
         */
        // 主会场
        setIntent(context, messageData, remoteViews, "1", R.id.iv_main_page);
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 跳去主会场");

        //主互动
        setIntent(context, messageData, remoteViews, "2", R.id.iv_main_crazy);
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 跳去主互动");

        //设置 去我的淘宝设置页面
        setIntent(context, messageData, remoteViews, "3", R.id.iv_setting);
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 关闭通知");

        //消息通道
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("双十一小助手",
                    "天猫双十一", NotificationManager.IMPORTANCE_HIGH);

            // 注册通道，注册后除非卸载再安装否则不改变
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId("双十一小助手");
            FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 设置通知channel");
        }

        builder.setCustomContentView(remoteViews);

        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 设置通知布局");
        Notification notification = builder.build();
//        notification.bigContentView = remoteViews;
        return notification;
    }

    /**
     * 设置点击事件的intent
     *
     * @param context
     * @param messageData
     * @param remoteViews
     * @param flag
     * @param viewId
     */
    private static void setIntent(Context context, MessageData messageData, RemoteViews remoteViews, String flag, int viewId) {
        Intent intentMainPage = new Intent(context, NotificationBroadcastReceiver.class);
        intentMainPage.putExtra(NOTICE_ID_KEY, ACTION_CLOSE_NOTICE);
        intentMainPage.putExtra(BROADCAST_FLAG, flag);
        intentMainPage.putExtra(LANDINGURL, messageData.ladningUrl);

        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intentMainPage, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
    }

    /**
     * 跳转页面  埋点
     *
     * @param context
     * @param landingUrl
     * @param arg1
     * @param openURL
     */
    public static void jumpClick(Context context, String landingUrl, String arg1, String openURL) {
        TrackUtils.sendFloatData(arg1, landingUrl, "", new HashMap<String, String>());

        Intent intent = new Intent();
        intent.setData(Uri.parse(openURL));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        FlowCustomLog.d(LOG_TAG, "NotificationBroadcastReceiver === jumpClick === arg1=" + arg1 + " openURL=" + openURL);
    }

}
