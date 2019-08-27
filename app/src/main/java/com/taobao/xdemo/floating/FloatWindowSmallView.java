package com.taobao.xdemo.floating;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.taobao.xdemo.R;
import com.taobao.xdemo.notification.MessageData;
import com.taobao.xdemo.utils.FlowCustomLog;

import java.lang.reflect.Field;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;


/**
 * @author bill
 * @Date on 2019-08-20
 * @Desc:
 */
public class FloatWindowSmallView extends LinearLayout {

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    private final int mScreenWidth;
    private final int mScreenHeight;

    private Context mContext;
    private final View mView;

    public FloatWindowSmallView(Context context) {
        super(context);
        mContext = context;

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        mView = findViewById(R.id.small_window_layout);
        viewWidth = mView.getLayoutParams().width;
        viewHeight = mView.getLayoutParams().height;


        // 获取屏幕宽高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();

                FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === onTouchEvent ACTION_DOWN = ");
                break;

            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();

                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();

                FlowCustomLog.d(LOG_TAG, "FloatWindowSmallView === onTouchEvent ACTION_MOVE = ");
                break;

            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    // todo 待更换  new MessageData()
                    MessageManager.instance().performClick(mContext, new MessageData());
                }

                //这里做动画贴边效果
                float centerX = event.getRawX() + getWidth() / 2f;
                float halfOfScreenWidth = mScreenWidth / 2f;

                /*if (centerX > halfOfScreenWidth) {
                    ObjectAnimator.ofFloat(this, "translationX", xInScreen, mScreenWidth - getWidth() - 200)
                            .setDuration(2500)
                            .start();
                    FlowCustomLog.d(LOG_TAG, "centerX = aaaaa");
                } else {
                    ObjectAnimator.ofFloat(this, "translationX", xInScreen, 0)
                            .setDuration(2500)
                            .start();
                    FlowCustomLog.d(LOG_TAG, "centerX = cccccc");
                }*/


                FlowCustomLog.d(LOG_TAG, "centerX = " + centerX + " halfOfScreenWidth = " + halfOfScreenWidth + "  mScreenWidth =" + mScreenWidth + "   viewWidth" + viewWidth);
                xInScreen = xInScreen + mScreenWidth;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateViewPosition();
                    }
                }, 100);

                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;

    }

}
