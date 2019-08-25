package com.taobao.xdemo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.taobao.xdemo.FlowCustomLog;
import com.taobao.xdemo.MainActivity;
import com.taobao.xdemo.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * @author bill
 * @Date on 2019-08-21
 * @Desc:
 */
public class NotificationUtils {

    static final String NOTICE_ID_KEY = "NOTICE_ID";
    static final String ACTION_CLOSE_NOTICE = "cn.campusapp.action.closenotice";

    /**
     * 绘制通知
     *
     * @param context
     * @return
     */
    public static Notification showNotification(Context context) {
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 绘制通知");

        // todo 曝光和点击事件  和push一致
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.push_close);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        // 自定义布局
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_bar);
        remoteViews.setTextViewText(R.id.tv_setting, "设置");
        remoteViews.setImageViewResource(R.id.iv_main_page, R.drawable.share);
        remoteViews.setImageViewResource(R.id.iv_main_crazy, R.drawable.pic);

        //点击事件
        // 主会场
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_main_page, pendingIntent);
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 跳去主会场");


        //主互动
        Intent intent1 = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode1 = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, requestCode1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_main_crazy, pendingIntent1);
        FlowCustomLog.d(LOG_TAG, "NotificationUtils === showNotification === 跳去主互动");


        //设置
        int requestCode2 = (int) SystemClock.uptimeMillis();
        Intent intent2 = new Intent(context, NotificationBroadcastReceiver.class);
        intent2.putExtra(NOTICE_ID_KEY, ACTION_CLOSE_NOTICE);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, requestCode2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_setting, pendingIntent2);
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
     * 创建通知
     *
     * @return
     */
    public static Notification getNotification(Context context) {

        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_bar);
//        mRemoteViews.setTextViewText(R.id.tv_double_11, "哈哈哈哈哈");

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
//        builder.setContent(mRemoteViews);

        builder.setContentText("hahah");
        builder.setContentTitle("hahah");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("AppTestNotificationId",
                    "AppTestNotificationName", NotificationManager.IMPORTANCE_HIGH);

            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("AppTestNotificationId");
        }

        // 常驻通知栏  清理通知还在  但是杀进程就不在了
        builder.setOngoing(true);

        return builder.build();
    }

    // 取消通知
    public static void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(R.string.app_name);
    }
}
