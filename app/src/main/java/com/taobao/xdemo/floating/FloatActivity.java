package com.taobao.xdemo.floating;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.taobao.xdemo.R;
import com.taobao.xdemo.notification.NotificationUtils;
import com.taobao.xdemo.utils.FlowCustomLog;
import com.taobao.xdemo.utils.utils;

import static com.taobao.xdemo.floating.MessageManager.ASSISTANT_DISAPPEAR;
import static com.taobao.xdemo.floating.MessageManager.ASSISTANT_NEW_MSG;


/**
 * 小助手设置界面
 */
public class FloatActivity extends AppCompatActivity {

    private static final int REQUEST_OVERLAY = 5004;
    public static final String LOG_TAG = "double11";
    private static final String TAG = "double11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);


       utils. checkUsageStateAccessPermission(this);


        // 是否支持小助手功能
        findViewById(R.id.tv_float_accsss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean supportAssisant = FloatUtils.isSupportAssisant(getApplicationContext());

                if (supportAssisant) {
                    Toast.makeText(getApplicationContext(), "已经支持小助手功能，点击下方的按钮获取权限吧!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "不支持小助手功能，升级手淘版本吧", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //打开通知栏
        findViewById(R.id.tv_open_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationUtils.openNotification(getApplicationContext());
            }
        });

        //取消通知
        findViewById(R.id.tv_cancle_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationUtils.closeNotification(getApplicationContext());
            }
        });

        // 申请权限
        findViewById(R.id.tv_float_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  boolean b = FloatUtils.checkFloatPermission(getApplicationContext());

                if (false) {
                    Toast.makeText(getApplicationContext(), "已经获取到权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "开始获取悬浮窗权限", Toast.LENGTH_SHORT).show();

                    // 跳转到其他应用层之上
                    RequestOverlayPermission(getApplicationContext());
                }*/


                // 跳转到其他应用层之上
                RequestOverlayPermission(getApplicationContext());
            }
        });

        // 开启小助手
        findViewById(R.id.tv_open_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = FloatUtils.checkFloatPermission(getApplicationContext());
                if (!b) {
                    Toast.makeText(getApplicationContext(), "没有相关权限，请申请权限", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "开始小助手服务，快去桌面瞅瞅哦", Toast.LENGTH_SHORT).show();
                FloatUtils.startFloatService(getApplicationContext());
            }
        });

        // 模拟小助手消息到达
        findViewById(R.id.tv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message message = new Message();
                message.what = ASSISTANT_NEW_MSG;
                message.obj = "wwww.taobao.com";
                MessageManager.instance().getHandler(getApplicationContext()).sendMessage(message);
            }
        });

        //关闭小助手
        findViewById(R.id.tv_close_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "关闭小助手服务，下次再见喽", Toast.LENGTH_SHORT).show();

                Message message = new Message();
                message.what = ASSISTANT_DISAPPEAR;
                MessageManager.instance().getHandler(getApplicationContext()).sendMessage(message);
            }
        });
    }

    // 动态请求悬浮窗权限   跳转到显示到其他APP 上层界面
    public void RequestOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            FlowCustomLog.d(LOG_TAG, "FloatActivity === onActivityResult === 大于23，没有权限 申请权限");

            String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_OVERLAY:
/*
                if (FloatPermissionUtils.getFloatPermissionStatus(getApplicationContext()) == 0) {
                    FlowCustomLog.d(LOG_TAG, "FloatActivity === onActivityResult === 授权OK");
                } else {
                    FlowCustomLog.d(LOG_TAG, "FloatActivity === onActivityResult === 授权失败");
                }*/

                //华为 Android 8.0系统返回结果延迟   vivo 6.0
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (FloatUtils.checkFloatPermission(getApplicationContext())) {
                            FlowCustomLog.d(LOG_TAG, "FloatActivity === onActivityResult === 授权OK");
                        } else {
                            FlowCustomLog.d(LOG_TAG, "FloatActivity === onActivityResult === 授权失败");
                        }
                    }
                }, 500);

                break;
        }
    }

}
