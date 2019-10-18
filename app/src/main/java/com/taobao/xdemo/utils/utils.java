package com.taobao.xdemo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Trace;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.taobao.xdemo.App;
import com.taobao.xdemo.MainActivity;
import com.taobao.xdemo.R;
import com.taobao.xdemo.smartlink.SnartLinkActivity;


/**
 * @author bill
 * @Date on 2019-06-11
 * @Desc:
 */
public class utils {

    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private static final String mPinShortcutId = "hankinhui_shortcut";
    private android.widget.LinearLayout viewtopmsgparent;


    // https://www.cnblogs.com/mengdd/p/3837592.html

    public static void addShortcut(Context context, String labelName, String labelId, int resId, String url) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int request = context.checkSelfPermission(ACTION_ADD_SHORTCUT);

            if (request != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{ACTION_ADD_SHORTCUT}, 1222);
                Log.e("lmtest", "没有权限");
            }

            ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);//获取shortcutManager
            //如果默认桌面支持requestPinShortcut（ShortcutInfo，IntentSender）方法，则返回TRUE。

          /*  List<ShortcutInfo> infos = shortcutManager.getPinnedShortcuts();
            for (int i = 0; i < infos.size(); i++) {
                ShortcutInfo info = infos.get(i);
                if (info.getId().equals(mPinShortcutId)) {
                    if (info.isEnabled()) {//当pinned shortcut为disable时，重复创建会导致应用奔溃
                        break;
                    } else {
                        return;
                    }
                }
            }*/

            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                Uri uri = Uri.parse(url);
                Intent shortCutIntent = new Intent(Intent.ACTION_VIEW, uri);//快捷方式启动页面
                shortCutIntent.setAction(Intent.ACTION_VIEW);

                //快捷方式创建相关信息。图标名字 labelId
                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, labelId)
                        .setIcon(Icon.createWithResource(context, resId))
                        .setShortLabel(labelName)
                        .setIntent(shortCutIntent)
                        .build();
                //创建快捷方式时候回调
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new
                        Intent(context, SnartLinkActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                shortcutManager.requestPinShortcut(shortcutInfo, pendingIntent.getIntentSender());

            } else {
                Toast.makeText(context, "设备不支持在桌面创建快捷图标！!!!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);

            // 不允许重复创建
            addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
            // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
            // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
            // 屏幕上没有空间时会提示
            // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式

            // 名字
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, labelName);

            // 图标
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(context, resId));

            // 设置关联程序
            Uri uri = Uri.parse(url);
            Intent launcherIntent = new Intent(Intent.ACTION_VIEW, uri);

            addShortcutIntent
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

            // 发送广播
            context.sendBroadcast(addShortcutIntent);
        }

        Toast.makeText(context, "创建快捷方式成功", Toast.LENGTH_SHORT).show();
    }


    private void removeShortcut(Context context, String name) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);

        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 设置关联程序
        Intent launcherIntent = new Intent(context, MainActivity.class)
                .setAction(Intent.ACTION_MAIN);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        context.sendBroadcast(intent);
    }

    /**
     * 创建View并启动动画
     */
    public static View getView(final Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_top_msg, null);

        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 900, 0, 0);
        view.setLayoutParams(layoutParams);

        final int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        view.setOnTouchListener(new View.OnTouchListener() {
            private float xInScreen;
            private float yInScreen;
            private int lastX = 0;
            private int lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int l, t, r, b;

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        lastY = (int) event.getY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        xInScreen = event.getX();
                        yInScreen = event.getY();

                        int moveY = (int) (event.getY() - lastY);
                        int moveX = (int) (event.getX() - lastX);

                        int height = view.getBottom() - view.getTop();

                        l = view.getLeft() + moveX;
                        t = view.getTop() + moveY;
                        r = view.getRight();
                        b = view.getBottom() + moveY;


                        if (l < -100) {
                            view.setVisibility(View.GONE);
                        }

                        if (t < 0) {
                            t = 0;
                            b = height;
                        }

                        if (r > screenWidth) {
                            r = view.getRight();
                        }

                        if (b > screenHeight) {
                            b = screenHeight;
                            t = screenHeight - height;
                        }

                        view.layout(l, t, r, b);

                        Log.e("lmtest", "l=" + l + " t=" + t + " r=" + r + " b=" + b + " moveY=" + moveY);

                        break;

                    case MotionEvent.ACTION_UP:
                        if (Math.abs(xInScreen - lastX) < 1 && Math.abs(yInScreen - lastY) < 1) {
                            Toast.makeText(context, "我被点击了。。。", Toast.LENGTH_SHORT).show();
                        }

                        Log.e("lmtest", "xInScreen - lastX=" + (xInScreen - lastX) + " yInScreen - lastY=" + (yInScreen - lastY));

                        break;
                }

                return true;
            }
        });

        return view;
    }
}
