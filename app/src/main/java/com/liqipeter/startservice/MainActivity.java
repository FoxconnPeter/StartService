package com.liqipeter.startservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private EditText etData;
    private MyService.Binder binder = null;
    private TextView tvout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnstartservice).setOnClickListener(this);
        findViewById(R.id.btnstopservice).setOnClickListener(this);
        etData=(EditText)findViewById(R.id.etData);
        findViewById(R.id.btnbingdservice).setOnClickListener(this);
        findViewById(R.id.btnendservice).setOnClickListener(this);
        findViewById(R.id.updata).setOnClickListener(this);
        tvout=(TextView)findViewById(R.id.tvout);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnstartservice:
                Intent i = new Intent(MainActivity.this,MyService.class);
                i.putExtra("data",etData.getText().toString());
                startService(i);
                break;
            case R.id.btnstopservice:
                stopService(new Intent(MainActivity.this,MyService.class));

                break;
            case R.id.btnbingdservice:
                bindService(new Intent(this,MyService.class),this, Context.BIND_AUTO_CREATE);

                break;
            case R.id.btnendservice:
                unbindService(this);

                break;
            case R.id.updata:
                if (binder!=null){
                    binder.setData(etData.getText().toString());
                }

                break;





        }

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {

        binder = (MyService.Binder) service;
        binder.getService().setCallback(new MyService.Callback() {
            @Override
            public void onDataChange(String data) {

                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("data",data);
                msg.setData(b);
                handler.sendMessage(msg);

            }
        });

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvout.setText(msg.getData().getString("data"));


        }
    };

}
