package com.taobao.xdemo.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author bill
 * @Date on 2019-08-21
 * @Desc:
 */
public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 开启前台服务   todo 待更换  new MessageData()
        startForeground(111, NotificationUtils.showNotification(this, new MessageData()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
