package com.taobao.xdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.taobao.flowcustoms.afc.utils.FlowCustomLog;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;

/**
 * ams hook
 */
public class AMSInvocationHandler implements InvocationHandler {

    private Object iamObject;
    private Context context;
    public static OnIntentRedirectListener intentRedirectListener;

    public interface OnIntentRedirectListener {
        boolean onExternalRedirect(Intent intent, String packageName, String componentName, Context context);
    }

    public AMSInvocationHandler(Object iamObject, Context context) {
        this.iamObject = iamObject;
        this.context = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        FlowCustomLog.d(LOG_TAG, "开始监听" + method.getName());


        if ("startActivity".equals(method.getName())) {
            FlowCustomLog.d(LOG_TAG, "ready to startActivity：" + method.getName());
            if (redirectIntent(args)) {
                return 0;
            }
        }

        try {
            return method.invoke(iamObject, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }

    }

    /**
     * 对跳出的intent进行拦截
     *
     * @param args
     * @return
     */
    private boolean redirectIntent(Object[] args) {
        try {
            int index = 0;
            //  找到我们启动时的intent
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }

            // 取出Intent
            Intent intent = (Intent) args[index];

            if (intent != null) {
                String packageName = "";
                String componentName = "";

                if (intent.getComponent() != null) {
                    packageName = intent.getComponent().getPackageName();
                    componentName = intent.getComponent().getClassName();
                } else {
                    ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, 0);
                    if ((resolveInfo != null) && (resolveInfo.activityInfo != null)) {
                        packageName = resolveInfo.activityInfo.packageName;
                        componentName = resolveInfo.activityInfo.name;
                    }
                }

                FlowCustomLog.d(LOG_TAG, "AMSInvocationHandler === packageName:" + packageName
                        + "  componentName:" + componentName + "  intent:" + intent);

                // 包名不同  为跳出  进行判断逻辑的拦截
                if (!TextUtils.isEmpty(packageName) && !TextUtils.equals(packageName, "com.taobao.taobao")) {
                    if (intentRedirectListener != null) {
                        if (!intentRedirectListener.onExternalRedirect(intent, packageName, componentName, context)) {
                            FlowCustomLog.d(LOG_TAG, "AMSInvocationHandler === redirectIntent === 外跳被拦截！！！");
                            return true;
                        }
                    }
                } else {
                    FlowCustomLog.d(LOG_TAG, "AMSInvocationHandler === redirectIntent === 内部跳转调用！！！");
                }
            }

            return false;
        } catch (Exception e) {
            FlowCustomLog.d(LOG_TAG, "AMSInvocationHandler === redirectIntent === 外跳拦截异常：" + e.toString());
        }

        return false;
    }
}
