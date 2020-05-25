package com.taobao.xdemo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import android.content.Context;
import android.os.Build;
import com.taobao.flowcustoms.afc.utils.FlowCustomLog;
import com.taobao.xdemo.AMSInvocationHandler;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * hook 工具类
 */
public class HookManager {

    /**
     * hook ams的方式
     * 可以同时hook startActivity  getApplicationContext().startActivity
     *
     * @param context
     * @throws Exception
     */
    public static void hookAMS(Context context) {
        int sdkInt = Build.VERSION.SDK_INT;
        FlowCustomLog.d(LOG_TAG, "hookAMS: sdkInt=" + sdkInt);
        if (sdkInt >= Build.VERSION_CODES.O) {
            hookAMSAfter26(context);
        } else {
            hookAmsBefore26(context);
        }
    }

    public static void hookAmsBefore26(Context context) {
        // 第一步：获取 IActivityManagerSingleton
        try {
            Class<?> forName = Class.forName("android.app.ActivityManagerNative");
            Field defaultField = forName.getDeclaredField("gDefault");
            defaultField.setAccessible(true);
            Object defaultValue = defaultField.get(null);

            Class<?> forName2 = Class.forName("android.util.Singleton");
            Field instanceField = forName2.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            Object iActivityManagerObject = instanceField.get(defaultValue);

            // 第二步：获取我们的代理对象，这里因为 IActivityManager 是接口，我们使用动态代理的方式
            Class<?> iActivity = Class.forName("android.app.IActivityManager");
            InvocationHandler handler = new AMSInvocationHandler(iActivityManagerObject, context);
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{iActivity}, handler);

            // 第三步：偷梁换柱，将我们的 proxy 替换原来的对象
            instanceField.set(defaultValue, proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookAMSAfter26(Context context) {
        // 第一步：获取 IActivityManagerSingleton
        try {
            Class<?> aClass = Class.forName("android.app.ActivityManager");
            Field declaredField = aClass.getDeclaredField("IActivityManagerSingleton");
            declaredField.setAccessible(true);
            Object value = declaredField.get(null);

            Class<?> singletonClz = Class.forName("android.util.Singleton");
            Field instanceField = singletonClz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            Object iActivityManagerObject = instanceField.get(value);

            // 第二步：获取我们的代理对象，这里因为 IActivityManager 是接口，我们使用动态代理的方式
            Class<?> iActivity = Class.forName("android.app.IActivityManager");
            InvocationHandler handler = new AMSInvocationHandler(iActivityManagerObject, context);
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new
                    Class<?>[]{iActivity}, handler);

            // 第三步：偷梁换柱，将我们的 proxy 替换原来的对象
            instanceField.set(value, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}