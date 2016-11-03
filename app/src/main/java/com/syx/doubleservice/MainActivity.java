package com.syx.doubleservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//是否是第一次启动
//        SharedPreferences mySharedPreferences = getSharedPreferences("oldId",
//                Activity.MODE_PRIVATE);
//        Boolean first = mySharedPreferences.getBoolean("First", true);
//        if (first)
//        {
//            SharedPreferences.Editor editor = mySharedPreferences.edit();
//            editor.putBoolean("First",false);
//            editor.commit();
//            Init();
//        }

        if ((!isServiceWorked(this,  "com.syx.doubleservice.ServiceTwo")) && (!isServiceWorked(this, "com.syx.doubleservice.PollingService")) && (!isServiceWorked(this, "com.syx.doubleservice.ServiceOne"))) {
            Init();
        }

    }

    private void Init() {
        Intent serviceOne = new Intent();
        serviceOne.setClass(MainActivity.this, ServiceOne.class);
        startService(serviceOne);

        PollingUtils.startPollingService(this, 2, PollingService.class, PollingService.ACTION);
        Toast.makeText(MainActivity.this, "在mainactivity中开启PollingService", Toast.LENGTH_SHORT).show();

        Intent serviceTwo = new Intent();
        serviceTwo.setClass(MainActivity.this, ServiceTwo.class);
        startService(serviceTwo);
    }

    public static boolean isServiceWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Stop polling service
        //   System.out.println("在MainActivity中Stop polling service...");
        //  PollingUtils.stopPollingService(this, ServiceOne.class, ServiceOne.ACTION);
    }
}
