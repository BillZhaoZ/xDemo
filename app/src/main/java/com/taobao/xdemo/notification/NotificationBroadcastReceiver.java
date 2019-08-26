package com.taobao.xdemo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.taobao.xdemo.FlowCustomLog;
import com.taobao.xdemo.floating.FloatUtils;
import com.taobao.xdemo.floating.MessageManager;
import com.taobao.xdemo.floating.TrackUtils;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;
import static com.taobao.xdemo.notification.NotificationUtils.ACTION_CLOSE_NOTICE;
import static com.taobao.xdemo.notification.NotificationUtils.BROADCAST_FLAG;
import static com.taobao.xdemo.notification.NotificationUtils.LANDINGURL;
import static com.taobao.xdemo.notification.NotificationUtils.NOTICE_ID_KEY;

/**
 * 接收pendingintent广播
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FlowCustomLog.d(LOG_TAG, "NotificationBroadcastReceiver === onReceive " + intent);
        String stringExtra = intent.getStringExtra(NOTICE_ID_KEY);

        // 关闭下拉通知栏
        FloatUtils.collapseStatusBar(context);

        if (ACTION_CLOSE_NOTICE.equals(stringExtra)) {
            String flag = intent.getStringExtra(BROADCAST_FLAG);
            String landingUrl = intent.getStringExtra(LANDINGURL);

            if (TextUtils.equals(flag, "1")) { //  主会场
                // A区域点击事件
                NotificationUtils.jumpClick(context, landingUrl, TrackUtils.ARG1_NOTIFICATION_A_CLICK, MessageManager.MAIN_PAGE);

            } else if (TextUtils.equals(flag, "2")) { // 主互动
                // B区域点击事件
                NotificationUtils.jumpClick(context, landingUrl, TrackUtils.ARG1_NOTIFICATION_B_CLICK, MessageManager.MAIN_CRAZY);

            } else if (TextUtils.equals(flag, "3")) { // 设置区域点击事件
                NotificationUtils.jumpClick(context, landingUrl, TrackUtils.ARG1_NOTIFICATION_DELETE_CLICK, MessageManager.MAIN_SETTINGS);
            }
        }
    }

}