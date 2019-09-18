package taobao.taobao.com.mytestdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Test {



    public static void main(String[] args) {

        // 8.12.0

        String version1 = "8.11.1.9999";

        String[] split = version1.split(".");

        String s = split[0];



    }




    /**
     * Description: 判断一个时间是否在一个时间段内 </br>
     *
     * @param nowTime   当前时间 </br>
     * @param beginTime 开始时间 </br>
     * @param endTime   结束时间 </br>
     */
    private static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }
}
