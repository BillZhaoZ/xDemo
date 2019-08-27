package com.taobao.xdemo.rom;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;


import com.taobao.xdemo.utils.FlowCustomLog;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import static com.taobao.xdemo.floating.FloatActivity.LOG_TAG;


/**
 * @author bill
 * @Date on 2019-08-20
 * @Desc:
 */
public class romUtils {

    // 华为 : EMUI
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui"; // "EmotionUI_3.0"

    // 小米 : MIUI
    private static final String KEY_MIUI_VERSION = "ro.build.version.incremental"; // "7.6.15"

    // OPPO : ColorOS
    private static final String KEY_COLOROS_ROM_VERSION = "ro.rom.different.version"; // "ColorOS2.1"

    // vivo : FuntouchOS   ro.vivo.os.version
    private static final String KEY_FUNTOUCHOS_DISPLAY_ID = "ro.vivo.os.build.display.id"; // "FuntouchOS_3.0"

    /**
     * 初始化 ROM 类型
     */
    public static String getRomType() {
        String systemProperty = "other";
        String mark = Build.MANUFACTURER.toLowerCase();

        if (mark.contains("huawei")) {
            //华为
            systemProperty = getSystemProperty(KEY_EMUI_VERSION, "");
        } else if (mark.contains("xiaomi")) {
            //小米
            systemProperty = getSystemProperty(KEY_MIUI_VERSION, "");
        } else if (mark.contains("oppo")) {
            // oppo
//            systemProperty = getSystemProperty(KEY_COLOROS_ROM_VERSION, "");
            systemProperty = getSystemProperty("ro.build.version.opporom", "");
        } else if (mark.contains("vivo")) {
            // vivo
            systemProperty = getSystemProperty(KEY_FUNTOUCHOS_DISPLAY_ID, "");
        } else {
            FlowCustomLog.e(LOG_TAG, "TFCCommonUtils === getRomType === 机型没有命中 =" + Build.MANUFACTURER);
        }

        if (!TextUtils.isEmpty(systemProperty)) {
            return systemProperty;
        }

        return "other";
    }

    public static String getSystemProperty(String key, String defaultValue) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 获取 build.prop 配置
            FileInputStream is = null;
            try {
                // 获取 build.prop 配置
                Properties buildProperties = new Properties();
                is = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                buildProperties.load(is);

                String s = (String) buildProperties.get(key);
                FlowCustomLog.e(LOG_TAG, "TFCCommonUtils === getSystemProperty === 小于8.0=" + s);

                return s;
            } catch (Exception e) {
                FlowCustomLog.e(LOG_TAG, "TFCCommonUtils === getSystemProperty === 小于8.0,获取异常：" +
                        e.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                Class<?> clz = Class.forName("android.os.SystemProperties");
                Method get = clz.getMethod("get", String.class, String.class);
                String invoke = (String) get.invoke(clz, key, defaultValue);
                FlowCustomLog.e(LOG_TAG, "TFCCommonUtils === getSystemProperty === 大于8.0=" + invoke);
                return invoke;
            } catch (Exception e) {
                FlowCustomLog.e(LOG_TAG, "TFCCommonUtils === getSystemProperty === 大于8.0，获取异常"
                        + e.getMessage());
            }
        }

        FlowCustomLog.e(LOG_TAG, "返回默认值");
        return defaultValue;
    }
}
