package taobao.taobao.com.mytestdemo;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * @author bill
 * @Date on 2018/12/24
 * @Desc:
 */
public class MyApplication extends Application {


    private SharedPreferences mSp;

    @Override
    public void onCreate() {
        super.onCreate();

       /* mSp = getApplicationContext().getSharedPreferences("tag", Context.MODE_PRIVATE);

        String string = mSp.getString("crash", "默认值");
        Log.e("haha", "读取存储 === " + string);*/
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

       /* SharedPreferences.Editor edit = mSp.edit();
        edit.putString("crash", "杀死了。。。");
        edit.apply();*/
    }
}
