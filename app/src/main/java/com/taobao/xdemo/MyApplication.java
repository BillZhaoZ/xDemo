package com.taobao.xdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bun.miitmdid.core.JLibrary;
import com.taobao.xdemo.oaid.MiitHelper;
import com.taobao.xdemo.utils.utils;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    /*当前对象的静态实例*/
    private static MyApplication instance;
    /*当前显示的Activity*/
    private Activity activity;

    private static String oaid;
    private static boolean isSupportOaid;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        JLibrary.InitEntry(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.instance = this;
        this.registerActivityLifecycleCallbacks(this);

        //获取OAID等设备标识符
        MiitHelper miitHelper = new MiitHelper(appIdsUpdater);
        miitHelper.getDeviceIds(getApplicationContext());
    }

    public static boolean isSupportOaid() {
        return isSupportOaid;
    }

    public static String getOaid() {
        return oaid;
    }

    public static void setIsSupportOaid(boolean isSupportOaid) {
        MyApplication.isSupportOaid = isSupportOaid;
    }

    private MiitHelper.AppIdsUpdater appIdsUpdater = new MiitHelper.AppIdsUpdater() {
        @Override
        public void OnIdsAvalid(@NonNull String ids) {
            Log.e("++++++ids: ", ids);
            oaid = ids;
        }
    };

    /**
     * 获取Application对象
     *
     * @return 返回一个App对象实例
     * @see MyApplication
     */
    public static MyApplication instance() {
        return MyApplication.instance;
    }

    /**
     * 显示View
     *
     * @param view 需要显示到Activity的视图
     */
    public void showView(View view) {
        /*Activity不为空并且没有被释放掉*/
        if (this.activity != null && !this.activity.isFinishing()) {
            /*获取Activity顶层视图,并添加自定义View*/
            ((ViewGroup) this.activity.getWindow().getDecorView()).addView(view);
        }
    }

    /**
     * 隐藏View
     *
     * @param view 需要从Activity中移除的视图
     */
    public void hideView(View view) {
        /*Activity不为空并且没有被释放掉*/
        if (this.activity != null && !this.activity.isFinishing()) {
            /*获取Activity顶层视图*/
            ViewGroup root = ((ViewGroup) this.activity.getWindow().getDecorView());
            /*如果Activity中存在View对象则删除*/
            if (root.indexOfChild(view) != -1) {
                /*从顶层视图中删除*/
                root.removeView(view);
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        /*获取当前显示的Activity*/
        this.activity = activity;

        View view = utils.getView(activity);
        showView(view);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        View view = utils.getView(activity);
        hideView(view);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}