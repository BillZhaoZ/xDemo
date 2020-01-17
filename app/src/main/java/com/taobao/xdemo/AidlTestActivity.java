package com.taobao.xdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.bill.myserver.MyAidlService;

public class AidlTestActivity extends AppCompatActivity {

    private TextView bindService;
    private TextView tvData;
    private MyAidlService myAIDLService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAIDLService = MyAidlService.Stub.asInterface(service);
            try {
                String str = myAIDLService.getString();
                tvData.setText(str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAIDLService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_test);

        bindService = findViewById(R.id.bind_service);
        TextView unbindService = (TextView) findViewById(R.id.unbind_service);
        tvData = (TextView) findViewById(R.id.tv_data);

        /**
         * 绑定服务
         */
        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.bill.myserver.MyService");
                //从 Android 5.0开始 隐式Intent绑定服务的方式已不能使用,所以这里需要设置Service所在服务端的包名
                intent.setPackage("com.bill.myserver");
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        /**
         * 解绑服务
         */
        unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvData.setText("服务已经解绑");
                unbindService(connection);
            }
        });
    }
}
