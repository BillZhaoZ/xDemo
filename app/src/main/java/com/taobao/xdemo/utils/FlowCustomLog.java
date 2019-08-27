package com.taobao.xdemo.utils;

import android.util.Log;

import java.io.File;

/**
 * 封装了对系统Log的调用
 * Created by LiDili on 16/12/21.
 */
public class FlowCustomLog {

    private static boolean enableLog = true;
    private static boolean sExists;

    // 添加开关控制是否转换log级别
    static {
        try {
            if (enableLog) {
                sExists = (new File("/data/local/tmp/", "tao_link_log_open")).exists();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setLogEnabled(boolean b) {
        enableLog = b;
    }

    public static void i(String tag, String msg) {
        if (enableLog) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (enableLog) {
            if (sExists) {
                Log.e(tag, msg);
            } else {
                Log.d(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (enableLog) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (enableLog) {
            Log.v(tag, msg);
        }
    }
}