package com.taobao.xdemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import org.intellij.lang.annotations.Language;

/**
 * @author bill
 * @Date on 2020/9/29
 * @Desc:
 */
public class MyUtils {

    @Language("JSON")
    private static String json = "{\"abtest\": \"hah\"}";

    private static String sReadPhoneState = Manifest.permission.READ_PHONE_STATE;

    public static String getImeiCard(Context context) {

        return "";
    }

    /**
     * 获取imei
     *
     * @return
     */
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
            e.printStackTrace();
        }

        return "";
    }

    @RequiresApi(api = VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    public static String getIMEIIndex(Context context, int index) {

        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = telephonyManager.getClass();
        Method getImei = null;

        try {
            getImei = clazz.getDeclaredMethod("getImei", int.class);//(int slotId)
            getImei.invoke(telephonyManager, 0); //卡1
            getImei.invoke(telephonyManager, 1); // 卡2

            Log.e("haha", "IMEI1 : " + getImei.invoke(telephonyManager, 0)); //卡1
            Log.e("haha", "IMEI2 : " + getImei.invoke(telephonyManager, 1)); //卡2

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "空的";

    }

}
