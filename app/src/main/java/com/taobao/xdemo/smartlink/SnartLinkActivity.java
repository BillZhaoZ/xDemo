package com.taobao.xdemo.smartlink;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.taobao.xdemo.R;


public class SnartLinkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.tv_call_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SnartLinkActivity.this, "点击拉端", Toast.LENGTH_SHORT).show();

                try {
                    Intent intent = new Intent();
                    intent.setAction("com.taobao.open.intent.action.GET");
//                    intent.setData(Uri.parse("alitbopen://m.taobao.com/tbopen/index.htm?h5Url=https%3A%2F%2Fh5.m.taobao.com%2Fbcec%2Fdahanghai-jump.html%3Fspm%3D2014.ugdhh.4019755093.10012-4117%26bc_fl_src%3Dgrowth_dhh_4019755093_10012-4117"));
                    intent.setData(Uri.parse("alitbopen://m.taobao.com/tbopen/index.htm?smartSource=dhh&h5Url=https%3A%2F%2Fdetail.m.tmall.com%2Fitem.htm%3Fid%3D564964406684%26spm%3Da2141.7631565.banner_21825005452.1%26skuId%3D3574394465600"));
                    startActivity(intent);

                    Log.e("MyTest", "开始发广播");
                } catch (Exception e) {

                    Toast.makeText(SnartLinkActivity.this, "请安装要跳转的APP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
