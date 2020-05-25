package com.taobao.xdemo.hook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.taobao.flowcustoms.afc.utils.FlowCustomLog;

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
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {iActivity}, handler);

            // 第三步：偷梁换柱，将我们的 proxy 替换原来的对象
            instanceField.set(defaultValue, proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookAMSAfter26(Context context) {

        if (Build.VERSION.SDK_INT >= 29) {

            try {
                Class<?> clazz = Class.forName("android.app.ActivityTaskManager");
                Field defaultFiled = clazz.getDeclaredField("IActivityTaskManagerSingleton");
                defaultFiled.setAccessible(true);
                Object defaultValue = defaultFiled.get(null);

                if (defaultValue == null) {
                    Log.i("testabc", "efaultValue==null");
                }
                //反射SingleTon
                Class<?> SingletonClass = Class.forName("android.util.Singleton");
                Field mInstance = SingletonClass.getDeclaredField("mInstance");
                mInstance.setAccessible(true);
                Object iActivityManagerObject = mInstance.get(defaultValue);
                if (iActivityManagerObject != null) {
                    Log.e("testabc", "IActivityTaskManagerSingleton 2 ");
                    //开始动态代理，用代理对象替换掉真实的ActivityManager，瞒天过海
                    Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityTaskManager");
                    AMSInvocationHandler handler = new AMSInvocationHandler(iActivityManagerObject, context);
                    Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[] {IActivityManagerIntercept}, handler);
                    //现在替换掉这个对象
                    mInstance.set(defaultValue, proxy);
                    Log.e("testabc", "IActivityTaskManagerSingleton 3 ");
                } else {
                    Log.e("testabc", "IActivityTaskManagerSingleton: is null ");
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {

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
                    Class<?>[] {iActivity}, handler);

                // 第三步：偷梁换柱，将我们的 proxy 替换原来的对象
                instanceField.set(value, proxy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}