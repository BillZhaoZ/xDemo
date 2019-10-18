package com.taobao.xdemo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.taobao.xdemo.floating.FloatActivity;
import com.taobao.xdemo.rom.romUtils;
import com.taobao.xdemo.smartlink.SnartLinkActivity;
import com.taobao.xdemo.utils.FlowCustomLog;

import static com.taobao.xdemo.utils.utils.addShortcut;

public class MainActivity extends AppCompatActivity {

    /*显示提示框按钮*/
    private Button showTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_transparebt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestOverlayPermission(getApplicationContext());
//                startActivity(new Intent(getApplicationContext(), Main3Activity.class));

                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), Main3Activity.class));
                    }
                }, 0);

            }
        });

        createAndStart();

        findViewById(R.id.tv_show_pop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });

        findViewById(R.id.tv_add_shortcut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();

                addShortcut(MainActivity.this, "我是测试icon 第一个", "11", R.drawable.link_semicircle
                        , "tbopen://m.taobao.com/tbopen/index.html?ext=default&clientId=2&materialType=1&carrierType=2&authorId=LlEowB0uPR8&clientVersion=6.5.2.9311&materialId=4xHkQouLbN0&itemId=lpjnOjEWStk&sourceType=2&linkUrl=https%3A%2F%2Fitem.taobao.com%2Fitem.htm%3Fid%3D596138545472&linkType=0&carrierId=8MImYufSxwA&visitorId=ycUl2KCsXP0&action=ali.open.nav&module=h5&appkey=24648319&TTID=2014_0_24648319%40baichuan_h5_0.3.8&source=linksdk&v=0.3.8&h5Url=https%3A%2F%2Fitem.taobao.com%2Fitem.htm%3Fid%3D596138545472%26params%3D%257B%2522ybhpss%2522%253A%2522dHRpZD0yMDE0XzBfMjQ2NDgzMTklNDBiYWljaHVhbl9oNV8wLjMuOA%25253D%25253D%2522%257D&params=%7B%22ybhpss%22%3A%22dHRpZD0yMDE0XzBfMjQ2NDgzMTklNDBiYWljaHVhbl9oNV8wLjMuOA%253D%253D%22%7D&backURL=kwai%3A%2F%2Faction%2FbringToFront");
            }
        });


        findViewById(R.id.tv_add_shortcut2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了222222", Toast.LENGTH_SHORT).show();

                addShortcut(MainActivity.this, "我是测试icon 第2个", "22", R.drawable.link_semicircle
                        , "http://wapp.m.taobao.com");
            }
        });

        // 智能分流
        findViewById(R.id.tv_call_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SnartLinkActivity.class));
            }
        });


        // rom信息
        final TextView viewById = findViewById(R.id.tv_rom);

        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyTest", "点击获取rom数据");
                final String romType = romUtils.getRomType();
                viewById.setText("机型rom信息：" + romType);
            }
        });


        // 小助手
        findViewById(R.id.tv_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FloatActivity.class));
            }
        });

    }

    private void showView() {
        createAndStart();
    }

    /**
     * 创建View并启动动画
     */
    @SuppressLint("InflateParams")
    private void createAndStart() {
        /*创建提示消息View*/
        final View view = LayoutInflater.from(this).inflate(R.layout.view_top_msg, null);
        /*创建属性动画,从下到上*/
        ObjectAnimator bottomToTop = ObjectAnimator.ofFloat(view, "translationY", 0, -dp2px(80)).setDuration(500);
        /*创建属性动画,从上到下*/
        ObjectAnimator topToBottom = ObjectAnimator.ofFloat(view, "translationY", -dp2px(80), 0).setDuration(500);
        /*初始化动画组合器*/
        AnimatorSet animator = new AnimatorSet();
        /*组合动画*/
        animator.play(bottomToTop).after(topToBottom).after(2000);
        /*添加动画结束回调*/
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                /*删除View*/
                App.instance().hideView(view);
            }
        });

        /*添加View到当前显示的Activity*/
        App.instance().showView(view);

        /*启动动画*/
        animator.start();
    }

    /**
     * 从dp单位转换为px
     *
     * @param dp dp值
     * @return 返回转换后的px值
     */
    private int dp2px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    /**
     * 动态请求悬浮窗权限   跳转到显示到其他APP 上层界面
     *
     * @param context
     */
    public void RequestOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {

            String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, 5004);
        }
    }

    private static String sReadPhoneState = Manifest.permission.READ_PHONE_STATE;


    public static String getIMEI(Context context) {
        if (context == null) {
            return "";
        }

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, sReadPhoneState) == PackageManager.PERMISSION_GRANTED) {
                return telephonyManager.getDeviceId();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{sReadPhoneState}, 122);
            }
        } catch (Exception e) {

        }

        return "";
    }
}
