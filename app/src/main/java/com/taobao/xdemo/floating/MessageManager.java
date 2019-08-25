package com.taobao.xdemo.floating;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.taobao.xdemo.FlowCustomLog;

import java.util.Calendar;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;


/**
 * @author bill
 * @Date on 2019-08-24
 * @Desc:
 */
public class MessageManager {

    private Context mContext;
    private boolean isShowNewMessage; // 小助手显示时，是否有新消息到达  需要处理
    private boolean isDisappear; // 小助手消失
    public static boolean isClickMainPage; //是否是点击去往主会场

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
        if (msg.what == 1) {
            FlowCustomLog.d(LOG_TAG, "MessageManager === handleMessage === 绘制小助手");

            //  小助手没有新消息   小助手不消失
            if (!isShowNewMessage && !isDisappear) {

                if (FloatUtils.isHome(mContext) && !MyWindowManager.isWindowShowing()) {
                    // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyWindowManager.createSmallWindow(mContext);
                        }
                    });
                } else if (!FloatUtils.isHome(mContext) && MyWindowManager.isWindowShowing()) {
                    // 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                           MyWindowManager.removeSmallWindow(mContext);
                            MyWindowManager.removeBigWindow(mContext);
                        }
                    });
                } else if (FloatUtils.isHome(mContext) && MyWindowManager.isWindowShowing()) {
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
        else if (msg.what == 2) {
            Object obj = msg.obj;
            FlowCustomLog.d(LOG_TAG, "MessageManager === handleAssMesssage === 新消息来啦,来个动画: obj=" + obj);
            isShowNewMessage = true;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "快点我，8s后我就消失了，抓紧时间", Toast.LENGTH_LONG).show();

                   FloatWindowSmallView smallWindow = MyWindowManager.createSmallWindow(mContext);

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
        else if (msg.what == 3) {
            FlowCustomLog.d(LOG_TAG, "MessageManager === handleAssMesssage === 关闭小助手");

            isDisappear = true;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    FloatWindowSmallView smallWindow = MyWindowManager.createSmallWindow(mContext);

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(smallWindow, "rotation", 720f);
                    objectAnimator.setDuration(2000);
                    objectAnimator.start();

                    mContext.stopService(new Intent(mContext, FloatWindowService.class));
                    MyWindowManager.removeSmallWindow(mContext);
                }
            });
        }
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    void performClick(Context mContext) {
        Toast.makeText(mContext, "点我干啥，我要跳页面了", Toast.LENGTH_SHORT).show();
        FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 打开大悬浮窗，同时关闭小悬浮窗");

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
            jumpPage(mContext, "tbopen://m.taobao.com/tbopen/index.html?source=auto&action=ali.open.nav&module=h5&h5Url=1111.tmall.com");

        } else {
            FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 其他日期："
                    + year + "年" + month + "月" + day + "号" + " " + hour + ":" + minute);

            if (isClickMainPage) {
                FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 非双十一当天，跳转主会场  isClickMain=" + isClickMainPage);

                // 主会场
                jumpPage(mContext, "tbopen://m.taobao.com/tbopen/index.html?source=auto&action=ali.open.nav&module=h5&h5Url=1111.tmall.com");

                isClickMainPage = !isClickMainPage;
            } else {
                FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 非双十一当天，跳转主互动 isClickMain=" + isClickMainPage);

                //主互动
                jumpPage(mContext, "tbopen://m.taobao.com/tbopen/index.html?source=auto&action=ali.open.nav&module=h5&h5Url=https%3a%2f%2fh5.m.taobao.com%2fbcec%2fdahanghai-jump.html%3fspm%3d2014.ugdhh.2200803433985.1001-5543%26bc_fl_src%3dgrowth_dhh_2200803433985_1001-5543%26activity_id%3d710&spm=2014.ugdhh.2200803433985.1001-5543&bc_fl_src=growth_dhh_2200803433985_1001-5543&materialid=1001&activity_id=710");

                isClickMainPage = !isClickMainPage;
            }

            FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === performClick === 非双十一当天，跳转后 isClickMain=" + isClickMainPage);
        }

//        MyWindowManager.createBigWindow(getContext());
//        MyWindowManager.removeSmallWindow(getContext());
    }

    private static void jumpPage(Context mContext, String jumpUrl) {
        Uri parse = Uri.parse(jumpUrl);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(parse);
        mContext.startActivity(intent);
    }

}
