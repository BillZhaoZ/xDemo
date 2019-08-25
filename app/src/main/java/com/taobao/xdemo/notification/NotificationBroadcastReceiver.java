package com.taobao.xdemo.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.taobao.xdemo.FlowCustomLog;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;
import static com.taobao.xdemo.notification.NotificationUtils.ACTION_CLOSE_NOTICE;
import static com.taobao.xdemo.notification.NotificationUtils.NOTICE_ID_KEY;

/**
 * 接收pendingintent广播
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FlowCustomLog.d(LOG_TAG, "NotificationBroadcastReceiver === onReceive " + intent);
        String stringExtra = intent.getStringExtra(NOTICE_ID_KEY);

        if (ACTION_CLOSE_NOTICE.equals(stringExtra)) {
            //处理点击事件
            context.stopService(new Intent(context, NotificationService.class));
        }
    }
}