package com.taobao.xdemo.floating;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import com.taobao.xdemo.FlowCustomLog;

import java.util.Timer;
import java.util.TimerTask;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * @author bill
 * @Date on 2019-08-20
 * @Desc: 小助手
 */
public class FloatWindowService extends Service {

    private Context mContext;

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);

            FlowCustomLog.d(LOG_TAG, "FloatWindowService === onStartCommand === 开启定时器，每隔0.5秒刷新一次");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;

        FlowCustomLog.d(LOG_TAG, "FloatWindowService === onDestroy === Service被终止的同时也停止定时器继续运行");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class RefreshTask extends TimerTask {

        @Override
        public void run() {
            // 发送小助手的绘制消息
            Message message = new Message();
            message.what = 1;
            MessageManager.instance().getHandler(mContext).sendMessage(message);
        }
    }

}
