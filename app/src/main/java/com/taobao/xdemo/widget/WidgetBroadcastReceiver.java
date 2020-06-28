package com.taobao.xdemo.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.xdemo.MainActivity;
import com.taobao.xdemo.notification.NotificationUtils;
import com.taobao.xdemo.utils.FlowCustomLog;

import static com.taobao.xdemo.widget.WidgetProvider.ACTION_CLOSE_NOTICE;
import static com.taobao.xdemo.widget.WidgetProvider.NOTICE_ID_KEY;

/**
 * 接收pendingintent广播
 * 处理自定义事件
 *
 * @author bill
 */
public class WidgetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {
            return;
        }

        String noticeId = intent.getStringExtra(NOTICE_ID_KEY);

        if (TextUtils.equals(ACTION_CLOSE_NOTICE, noticeId)) {
            FlowCustomLog.d("WidgetProvider", "updateAllAppWidgets === 跳转 ");

            intent.setClass(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}