package com.syx.doubleservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ServiceOne extends Service {
    public final static String TAG = "com.syx.doubleservice.ServiceOne";
    Timer timer = new Timer();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.start();
        return START_STICKY;
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
//                    Log.e(TAG, "ServiceOne Run: "+System.currentTimeMillis());
                    boolean b = MainActivity.isServiceWorked(ServiceOne.this, "com.syx.doubleservice.ServiceTwo");
                    boolean c=MainActivity.isServiceWorked(ServiceOne.this,"com.syx.doubleservice.PollingService");
                    if (!b) {
                        Intent service = new Intent(ServiceOne.this, ServiceTwo.class);
                        startService(service);
                        //     PollingUtils.startPollingService(ServiceOne.this, 20, PollingService.class, PollingService.ACTION);
//                        Log.e(TAG, "Start ServiceTwo");
                    }else if(!c)
                    {
                        PollingUtils.startPollingService(ServiceOne.this, 2, PollingService.class, PollingService.ACTION);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
