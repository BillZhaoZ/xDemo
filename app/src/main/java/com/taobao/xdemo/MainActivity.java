package com.taobao.xdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.taobao.xdemo.floating.FloatActivity;
import com.taobao.xdemo.rom.romUtils;
import com.taobao.xdemo.smartlink.SnartLinkActivity;

import static com.taobao.xdemo.utils.addShortcut;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_add_shortcut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();

                addShortcut(MainActivity.this, "我是测试icon 第一个", "11", R.drawable.link_semicircle
                        , "tbopen://m.taobao.com/tbopen/index.html?ext=default&clientId=2&materialType=1&carrierType=2&authorId=LlEowB0uPR8&clientVersion=6.5.2.9311&materialId=4xHkQouLbN0&itemId=lpjnOjEWStk&sourceType=2&linkUrl=https%3A%2F%2Fitem.taobao.com%2Fitem.htm%3Fid%3D596138545472&linkType=0&carrierId=8MImYufSxwA&visitorId=ycUl2KCsXP0&action=ali.open.nav&module=h5&appkey=24648319&TTID=2014_0_24648319%40baichuan_h5_0.3.8&source=linksdk&v=0.3.8&h5Url=https%3A%2F%2Fitem.taobao.com%2Fitem.htm%3Fid%3D596138545472%26params%3D%257B%2522ybhpss%2522%253A%2522dHRpZD0yMDE0XzBfMjQ2NDgzMTklNDBiYWljaHVhbl9oNV8wLjMuOA%25253D%25253D%2522%257D&params=%7B%22ybhpss%22%3A%22dHRpZD0yMDE0XzBfMjQ2NDgzMTklNDBiYWljaHVhbl9oNV8wLjMuOA%253D%253D%22%7D&backURL=kwai%3A%2F%2Faction%2FbringToFront");
            }
        });


        findViewById(R.id.tv_add_shortcut2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了222222", Toast.LENGTH_SHORT).show();

                addShortcut(MainActivity.this, "我是测试icon 第2个", "22", R.drawable.link_semicircle
                        , "http://wapp.m.taobao.com");
            }
        });

        // 智能分流
        findViewById(R.id.tv_call_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SnartLinkActivity.class));
            }
        });


        // rom信息
        final TextView viewById = findViewById(R.id.tv_rom);

        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MyTest", "点击获取rom数据");
                final String romType = romUtils.getRomType();
                viewById.setText("机型rom信息：" + romType);
            }
        });


        // 小助手
        findViewById(R.id.tv_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FloatActivity.class));
            }
        });

    }
}
