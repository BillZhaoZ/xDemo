package com.taobao.xdemo.floating;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.taobao.xdemo.notification.MessageData;
import com.taobao.xdemo.utils.FlowCustomLog;

import java.util.Calendar;
import java.util.HashMap;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * @author bill
 * @Date on 2019-08-24
 * @Desc:
 */
public class MessageManager {

    // todo 此处是否需要交给统跳？？？
    public static final String MAIN_PAGE = "tbopen://m.taobao.com/tbopen/index.html?source=auto&action=ali.open.nav&module=h5&h5Url=1111.tmall.com";
    public static final String MAIN_CRAZY = "tbopen://m.taobao.com/tbopen/index.html?source=auto&action=ali.open.nav&module=h5&h5Url=https%3A%2F%2Fmain.m.taobao.com%2Fdetail%2Findex.html%3Fid%3D592234074086%26alipay2taoBanner%3D10.0%26alipay2TaoBanner%3D10.0";
    public static final String MAIN_SETTINGS = "tbopen://m.taobao.com/tbopen/index.html?source=auto&action=ali.open.nav&module=h5&h5Url=http%3A%2F%2Fm.taobao.com%2Fgo%2Fmytaobaocommonsettings";

    private Context mContext;
    private boolean isShowNewMessage; // 小助手显示时，是否有新消息到达  需要处理
    private boolean isDisappear; // 小助手消失

    public static final int ASSISTANT_SHOW = 1;
    public static final int ASSISTANT_NEW_MSG = 2;
    public static final int ASSISTANT_DISAPPEAR = 3;

    public static MessageManager instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static MessageManager instance = new MessageManager();
    }

    private MessageManager() {
    }

    public Handler getHandler(Context mContext) {
        this.mContext = mContext;
        return handler;
    }

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            handleAssMesssage(msg);
            return false;
        }
    });

    /**
     * 处理handler发送过来的消息
     *
     * @param msg
     */
    private void handleAssMesssage(Message msg) {

        // 显示小助手
        if (msg.what == ASSISTANT_SHOW) {
            FlowCustomLog.d(LOG_TAG, "MessageManager === handleMessage === 绘制小助手");

            //  小助手没有新消息   小助手不消失
            if (!isShowNewMessage && !isDisappear) {

                if (FloatUtils.isHome(mContext) && !FloatAssistantManager.isWindowShowing()) {
                    // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            // todo 待更换  new MessageData()
                            FloatAssistantManager.createSmallWindow(mContext, new MessageData());
                        }
                    });

                } else if (!FloatUtils.isHome(mContext) && FloatAssistantManager.isWindowShowing()) {
                    // 当前界面不是桌面， 且有悬浮窗显示，则移除悬浮窗。
                    // TODO  手淘在前台 是否显示小助手

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            FloatAssistantManager.removeSmallWindow(mContext);
                            FloatAssistantManager.removeBigWindow(mContext);
                        }
                    });

                } else if (FloatUtils.isHome(mContext) && FloatAssistantManager.isWindowShowing()) {
                    // 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        }

        //消息到了，显示小助手  做个动画   交互态期间，点击跳转指定页面
        else if (msg.what == ASSISTANT_NEW_MSG) {
            Object obj = msg.obj;
            FlowCustomLog.d(LOG_TAG, "MessageManager === handleAssMesssage === 新消息来啦,来个动画: obj=" + obj);
            isShowNewMessage = true;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "快点我，8s后我就不动了，抓紧时间点啊。。。", Toast.LENGTH_LONG).show();

                    // todo 待更换  new MessageData()
                    FloatWindowSmallView smallWindow = FloatAssistantManager.createSmallWindow(mContext, new MessageData());

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(smallWindow, "rotation", 720f);
                    objectAnimator.setDuration(8000);
                    objectAnimator.start();
                }
            });

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    isShowNewMessage = false;
                    FlowCustomLog.d(LOG_TAG, "MessageManager === handleAssMesssage === 标记位重置，isShowNewMessage =false");
                }
            }, 8000);
        }

        //小助手消失
        else if (msg.what == ASSISTANT_DISAPPEAR) {
            FlowCustomLog.d(LOG_TAG, "MessageManager === handleAssMesssage === 关闭小助手消息到达");
            isDisappear = true;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    // todo 待更换  new MessageData()
                    FloatWindowSmallView smallWindow = FloatAssistantManager.createSmallWindow(mContext, new MessageData());

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(smallWindow, "rotation", 720f);
                    objectAnimator.setDuration(2000);
                    objectAnimator.start();

                    mContext.stopService(new Intent(mContext, FloatWindowService.class));
                    FloatAssistantManager.removeSmallWindow(mContext);
                }
            });
        }
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    void performClick(Context mContext, MessageData messageData) {
        Toast.makeText(mContext, "点我干啥，我要跳页面了", Toast.LENGTH_SHORT).show();
        FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 打开大悬浮窗，同时关闭小悬浮窗");

        // 点击事件
        TrackUtils.sendFloatData(TrackUtils.ARG1_ASSISTANT_CLICK, messageData.ladningUrl, "", new HashMap<String, String>());

        Calendar cal = Calendar.getInstance();// 当前日期
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
        final int start = 1;// 起始时间 0:01的分钟数
        final int end = 24 * 60;// 结束时间 24:00的分钟数

        // 2019.10.21 00：00 -2019.11.11 00：00之间，点击小助手轮换默认跳转主互动&主会场，11.11当天，默认点击都只跳转主会场。
        if (year == 2019 && month == 11 && day == 11 && minuteOfDay >= start && minuteOfDay <= end) {
            FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 双十一当天");

            // 双十一当天  跳主会场
            jumpPage(mContext, MAIN_PAGE);
        } else {
            FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 其他日期："
                    + year + "年" + month + "月" + day + "号" + " " + hour + ":" + minute);

            //主互动
            jumpPage(mContext, MAIN_CRAZY);
        }
    }

    /**
     * 跳转页面
     *
     * @param mContext
     * @param jumpUrl
     */
    private static void jumpPage(Context mContext, String jumpUrl) {
        Uri parse = Uri.parse(jumpUrl);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(parse);
        mContext.startActivity(intent);
    }

}
