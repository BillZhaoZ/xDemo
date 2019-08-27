package com.taobao.xdemo.floating;

import java.util.HashMap;
import java.util.Map;

/**
 * 智能拉端埋点工具类
 */
public class TrackUtils {

    // 业务埋点
    public static final String ARG1_NOTIFICATION_EXPOSE = "notification_expose";   // 通知栏曝光事件
    public static final String ARG1_NOTIFICATION_A_CLICK = "notification_A_click"; // 通知栏A区域点击事件
    public static final String ARG1_NOTIFICATION_B_CLICK = "notification_B_click"; // 通知栏B区域点击事件
    public static final String ARG1_NOTIFICATION_DELETE_CLICK = "notification_delete_click"; // 通知栏取消按钮点击事件
    public static final String ARG1_ASSISTANT_EXPOSE = "assistant_expose";          // 小助手曝光事件
    public static final String ARG1_ASSISTANT_CLICK = "assistant_click";            // 小助手点击事件


    // 性能埋点
    public static final String ARG1_SMART_REQUEST_INTERVAL = "smart_request_interval"; // 请求耗时
    public static final String ARG1_SMART_LINK_INTERVAL = "smart_link_interval"; // 主逻辑耗时
    public static final String ARG1_CHECK_CALL_TIME_INTERVAL = "check_call_time"; // 检测和拉起APP耗时


    /**
     * 业务埋点
     */
    public static void sendFloatData(final String arg1, final String arg2, final String arg3,
                                     Map<String, String> properties) {
        if (properties == null) {
            properties = new HashMap<>();
        }

       /* UTOriginalCustomHitBuilder builder = new UTOriginalCustomHitBuilder("Page_FlowCustoms",
                1013, arg1, arg2, arg3, properties);
        final Map<String, String> build = builder.build();
        FlowCustomLog.d(LOG_TAG_SMART, "arg1: " + arg1 + "  arg2: " + arg2 + "  arg3: " + arg3 + " args = " + build);

        HandlerUtils.instance.postNonUIThread(new Runnable() {
            @Override
            public void run() {
                UTAnalytics.getInstance().getDefaultTracker().send(build);
            }
        });*/
    }

    /**
     * 性能埋点
     *
     * @param arg1
     */
    public static void sendFloatTime(final String arg1, final long startTime, final long endTime) {
        String arg2 = (endTime - startTime) + "";
        Map<String, String> properties = new HashMap<>();

       /* UTOriginalCustomHitBuilder builder = new UTOriginalCustomHitBuilder("Page_FlowCustoms",
                1013, arg1, arg2, "", properties);
        final Map<String, String> build = builder.build();

        FlowCustomLog.d(LOG_TAG_SMART, "arg1: " + arg1 + " 耗时 =" + arg2);

        HandlerUtils.instance.postNonUIThread(new Runnable() {
            @Override
            public void run() {
                UTAnalytics.getInstance().getDefaultTracker().send(build);
            }
        });*/
    }
}