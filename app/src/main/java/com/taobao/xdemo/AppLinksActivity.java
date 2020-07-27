package com.taobao.xdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author bill
 */
public class AppLinksActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_links2);

        Toast.makeText(this, "我是applinks过来的", Toast.LENGTH_SHORT).show();
    }
}