package com.taobao.xdemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        final TextView viewById = findViewById(R.id.tv_text);

        findViewById(R.id.tv_text).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Long timeStamp = System.currentTimeMillis();  //获取当前时间戳
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));

                viewById.setText("currentTimeMillis=" + timeStamp + "\n  sd=" + sd);
            }
        });
    }
}
