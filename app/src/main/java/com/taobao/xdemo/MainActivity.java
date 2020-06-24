package com.taobao.xdemo;

import java.util.HashMap;

import com.alibaba.openid.OpenDeviceId;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.taobao.xdemo.floating.FloatActivity;
import com.taobao.xdemo.hook.ActivityTaskHook;
import com.taobao.xdemo.rom.romUtils;
import com.taobao.xdemo.rxjava.ErrorBean;
import com.taobao.xdemo.rxjava.RxBusHelper;
import com.taobao.xdemo.rxjava.RxJavaManager;
import com.taobao.xdemo.smartlink.SnartLinkActivity;
import com.taobao.xdemo.utils.FlowCustomLog;
import com.taobao.xdemo.utils.utils;
import com.taobao.xdemo.utils.utils.FlowType;
import io.reactivex.disposables.CompositeDisposable;

import static com.taobao.xdemo.utils.utils.addShortcut;

public class MainActivity extends AppCompatActivity {

    /*显示提示框按钮*/
    private Button showTips;

    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String main = getIntent().getExtras().getString("main");
            Toast.makeText(this, main, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        findViewById(R.id.tv_rx_java).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RxJavaManager rxJavaManager = new RxJavaManager();

                // 1、Demo
                //rxJavaManager.observable.subscribe(rxJavaManager.observer);

                // 2.定时器
                //rxJavaManager.observableTime.subscribe(rxJavaManager.observerTime);

                // 3.指定范围
                //rxJavaManager.observableRanger.subscribe(rxJavaManager.observerRanger);

                // 4.flatmap
                //RxJavaManager.actionFlatMap();

               /* RxBusHelper.post("hahahaha");

                RxBusHelper.doOnMainThread(String.class, disposables, new RxBusHelper.OnEventListener<String>() {
                    @Override
                    public void onEvent(String s) {
                        TextView viewById = findViewById(R.id.tv_rx_java);
                        viewById.setText(s);
                    }

                    @Override
                    public void onError(ErrorBean errorBean) {

                    }
                });*/

                String s =
                    "https://equity.tmall.com/tm?spm=a2o5r.9022594.0"
                        + ".0&agentId=636202&_bind=true&bc_fl_src=tmall_market_llb_1_800826&llbPlatform=_pube&llbOsd"
                        + "=1&afc_userId=3571644191&scheme=tbopen&oldUserId=3571644191&deviceLevel=-1&hasLoginToken"
                        + "=true&afc_id=afc_link%5Ecom.ss.android.ugc"
                        + ".aweme%5Enbc%5Ee38b369b-820b-4e39-8f85-b9716321c6de_1591537622204&launchType=COLD&afcflow"
                        + "=com.ss.android.ugc.aweme&isNeedHome=0&afcBackUrl=https%3A%2F%2Fm.taobao.com%2Findex"
                        + ".htm%3Fscrollto%3Drecmd%26target%3Dguess%26recmdparams%3D%257b%2522tabindex%2522%253a0"
                        + "%252c%2522bizparams%2522%253a%257b%2522outPushPlanId%2522%253a%25223YfuAj%2522%252c"
                        + "%2522test%2522%253a%2522testvalue%2522%257d%257d%26tppabid%3D170722%26pvid%3D186e2043-4f26"
                        + "-4fd6-9643-0d9caa3a95af%26bucketid%3DGuDing%26_afc_params_json%3D%257B%2522tppabid%2522"
                        + "%253A%2522170722%2522%252C%2522appKey%2522%253A%2522wild_baichuanpingtai_appkey%2522%252C"
                        + "%2522bc_fl_src%2522%253A%2522unknow%2522%252C%2522shopId%2522%253A%2522unknow%2522%257D"
                        + "%26_afc_params_kv%3DappKey%2501wild_baichuanpingtai_appkey%2502bc_fl_src%2501unknow"
                        + "%2502shopId%2501unknow%2502tppabid%2501170722%26launchType%3DCOLD";

                Uri mUri = Uri.parse(s);
                String afcBackUrl = mUri.getQueryParameter("afcBackUrl");

                String targetUri = mUri.getQueryParameter("afcBackUrl");

                String isNeedHome = mUri.getQueryParameter("isNeedHome");
                Uri.Builder builder = Uri.parse(targetUri).buildUpon();

                Uri isNeedHome1;

                if (!TextUtils.isEmpty(isNeedHome)) {
                    isNeedHome1 = builder.appendQueryParameter("isNeedHome", isNeedHome).build();
                } else {
                    isNeedHome1 = builder.build();
                }

                FlowCustomLog.d("Test", "TbFcLinkInit === oaid= " + isNeedHome1);

            }
        });

        findViewById(R.id.tv_hook).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               /* HookManager.hookAMS(MainActivity.this);

                FlowCustomLog.d(LOG_TAG, "开始监听 === registerOutHook === 开始hook");

                AMSInvocationHandler.intentRedirectListener = new AMSInvocationHandler.OnIntentRedirectListener() {
                    @Override
                    public boolean onExternalRedirect(Intent intent, String packageName, String componentName,
                        Context context) {
                        FlowCustomLog.d(LOG_TAG, "开始监听 === registerOutHook === 进行跳出拦截");

                        return true;
                    }
                };*/

                ActivityTaskHook taskHook = new ActivityTaskHook(getApplicationContext());
                taskHook.hookService();
            }
        });

        // 长按
        if (VERSION.SDK_INT >= VERSION_CODES.N_MR1) {
            utils.setupShortcuts(MainActivity.this);
        }

       /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("嗨，收到你的专属福利")
                .setPositiveButton("马上查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // 导航
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.create().show();*/

        findViewById(R.id.tv_time).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimeActivity.class));
            }
        });

        // 读取剪切板
        findViewById(R.id.tv_clipboard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = readClipBoard(MainActivity.this);
                Toast.makeText(MainActivity.this, "读取到了：" + s, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.tv_aidl).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AidlTestActivity.class));
            }
        });

        findViewById(R.id.tv_afc_id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("is_link", "true");
                utils.handleFlowParams(FlowType.LINK, "tbopen://", stringStringHashMap);
            }
        });

        // 获取oaid
        findViewById(R.id.tv_oaid).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String oaid = OpenDeviceId.getOAID(MainActivity.this);
                            FlowCustomLog.d("Test", "TbFcLinkInit === oaid= " + oaid);
                        }
                    }).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.tv_transparebt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestOverlayPermission(getApplicationContext());
                //                startActivity(new Intent(getApplicationContext(), Main3Activity.class));

                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), BottomActivity.class));
                    }
                }, 0);

            }
        });

        createAndStart();

        findViewById(R.id.tv_show_pop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });

        findViewById(R.id.tv_add_shortcut).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();

                addShortcut(MainActivity.this, "我是测试icon 第一个", "11", R.drawable.link_semicircle
                    ,
                    "tbopen://m.taobao.com/tbopen/index.html?ext=default&clientId=2&materialType=1&carrierType=2"
                        + "&authorId=LlEowB0uPR8&clientVersion=6.5.2"
                        + ".9311&materialId=4xHkQouLbN0&itemId=lpjnOjEWStk&sourceType=2&linkUrl=https%3A%2F%2Fitem"
                        + ".taobao.com%2Fitem.htm%3Fid%3D596138545472&linkType=0&carrierId=8MImYufSxwA&visitorId"
                        + "=ycUl2KCsXP0&action=ali.open.nav&module=h5&appkey=24648319&TTID=2014_0_24648319"
                        + "%40baichuan_h5_0.3.8&source=linksdk&v=0.3.8&h5Url=https%3A%2F%2Fitem.taobao.com%2Fitem"
                        + ".htm%3Fid%3D596138545472%26params%3D%257B%2522ybhpss%2522%253A"
                        + "%2522dHRpZD0yMDE0XzBfMjQ2NDgzMTklNDBiYWljaHVhbl9oNV8wLjMuOA%25253D%25253D%2522%257D&params"
                        + "=%7B%22ybhpss%22%3A%22dHRpZD0yMDE0XzBfMjQ2NDgzMTklNDBiYWljaHVhbl9oNV8wLjMuOA%253D%253D%22"
                        + "%7D&backURL=kwai%3A%2F%2Faction%2FbringToFront");
            }
        });

        findViewById(R.id.tv_add_shortcut2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了222222", Toast.LENGTH_SHORT).show();

                addShortcut(MainActivity.this, "我是测试icon 第2个", "22", R.drawable.link_semicircle
                    , "http://wapp.m.taobao.com");
            }
        });

        // 智能分流
        findViewById(R.id.tv_call_ad).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SnartLinkActivity.class));
            }
        });

        // rom信息
        final TextView viewById = findViewById(R.id.tv_rom);

        viewById.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyTest", "点击获取rom数据");
                final String romType = romUtils.getRomType();
                viewById.setText("机型rom信息：" + romType);
            }
        });

        // 小助手
        findViewById(R.id.tv_float).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FloatActivity.class));
            }
        });

    }

    /**
     * 读取剪切板
     */
    private String readClipBoard(Context context) {
        if (context == null) {
            return "";
        }

        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

        // 检查剪贴板是否有内容
        if (!clipboard.hasPrimaryClip()) {
            return "";
        }

        ClipData clipData = clipboard.getPrimaryClip();
        int count = clipData == null ? 0 : clipData.getItemCount();

        if (count > 0 && clipData.getItemAt(0) != null && clipData.getItemAt(0).getText() != null) {
            return clipData.getItemAt(0).getText().toString();
        } else {
            return "";
        }
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
                MyApplication.instance().hideView(view);
            }
        });

        /*添加View到当前显示的Activity*/
        MyApplication.instance().showView(view);

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
        return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 动态请求悬浮窗权限   跳转到显示到其他APP 上层界面
     *
     * @param context
     */
    public void RequestOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {

            String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, 5004);
        }
    }

    private static String sReadPhoneState = Manifest.permission.READ_PHONE_STATE;

    public static String getIMEI(Context context) {
        if (context == null) {
            return "";
        }

        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, sReadPhoneState) == PackageManager.PERMISSION_GRANTED) {
                return telephonyManager.getDeviceId();
            } else {
                ActivityCompat.requestPermissions((Activity)context, new String[] {sReadPhoneState}, 122);
            }
        } catch (Exception e) {

        }

        return "";
    }

}
