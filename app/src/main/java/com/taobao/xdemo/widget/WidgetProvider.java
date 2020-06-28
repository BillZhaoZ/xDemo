package com.taobao.xdemo.widget;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.taobao.xdemo.MainActivity;
import com.taobao.xdemo.R;
import com.taobao.xdemo.TimeActivity;
import com.taobao.xdemo.notification.NotificationBroadcastReceiver;
import com.taobao.xdemo.utils.FlowCustomLog;
import kotlin.UByte;

/**
 * @author bill
 * @Date on 2020/6/22
 * @Desc:
 */
public class WidgetProvider extends AppWidgetProvider {

    // 更新 widget 的广播对应的action
    private final String ACTION_UPDATE_ALL = "com.lyl.widget.UPDATE_ALL";
    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Set idsSet = new HashSet();

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        final String action = intent.getAction();

        FlowCustomLog.d("WidgetProvider", "onReceive === 接收窗口小部件点击时发送的广播 ");

        updateAllAppWidgets(context, AppWidgetManager.getInstance(context), idsSet);
    }

    // 更新所有的 widget
    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, Set set) {
        // widget 的id
        int appID;
        // 迭代器，用于遍历所有保存的widget的id
        Iterator it = set.iterator();

        // TODO:可以在这里做更多的逻辑操作，比如：数据处理、网络请求等。然后去显示数据

        while (it.hasNext()) {
            appID = ((Integer)it.next()).intValue();

            // 获取 example_appwidget.xml 对应的RemoteViews
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            // 设置点击按钮对应的PendingIntent：即点击按钮时，发送广播。
            remoteView.setOnClickPendingIntent(R.id.widget_btn_open, getOpenPendingIntent(context));

            // 更新 widget
            appWidgetManager.updateAppWidget(appID, remoteView);
        }
    }

    public static String NOTICE_ID_KEY = "NOTICE_ID";
    public static String ACTION_CLOSE_NOTICE = "com.taobao.action.close.notice";

    /**
     * 获取 打开 MainActivity 的 PendingIntent
     */
    private PendingIntent getOpenPendingIntent(Context context) {
        //intent.setClass(context, TimeActivity.class);

        Intent intent = new Intent(context, WidgetBroadcastReceiver.class);

        intent.putExtra("main", "这句话是我从桌面点开传过去的。");
        //PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        intent.putExtra(NOTICE_ID_KEY, ACTION_CLOSE_NOTICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法，可添加多次但只第一次调用
     */
    @Override
    public void onEnabled(Context context) {
        // 在第一个 widget 被创建时，开启服务
        //Intent intent = new Intent(context, WidgetService.class);
        //context.startService(intent);

        FlowCustomLog.d("WidgetProvider", "onEnabled === 窗口小部件第一次添加到桌面");

        super.onEnabled(context);
    }

    // 当 widget 被初次添加 或者 当 widget 的大小被改变时，被调用
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle
        newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        FlowCustomLog.d("WidgetProvider", "onAppWidgetOptionsChanged === 初次添加小窗口，或者小窗口大小被改变。。。");
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);

        FlowCustomLog.d("WidgetProvider", "onRestored === 恢复小窗口。。。");
    }

    /**
     * 每次窗口小部件被点击更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        FlowCustomLog.d("WidgetProvider", "onUpdate === 小窗口被点击了 = " + appWidgetIds);

        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (int appWidgetId : appWidgetIds) {
            idsSet.add(Integer.valueOf(appWidgetId));
        }
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        FlowCustomLog.d("WidgetProvider", "onDeleted === 小窗口被删除了 = " + appWidgetIds);

        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法，注意是最后一个
     */
    @Override
    public void onDisabled(Context context) {
        FlowCustomLog.d("WidgetProvider", "onDeleted === 最后一个小窗口被删除了");

        // 在最后一个 widget 被删除时，终止服务
        Intent intent = new Intent(context, WidgetService.class);
        context.stopService(intent);
        super.onDisabled(context);
    }
}
