package com.syx.doubleservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ServiceTwo extends Service {
  //  public final static String TAG = "com.syx.myapplication.ServiceTwo";
  Timer timer = new Timer();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.start();
        return START_REDELIVER_INTENT;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {

            TimerTask task = new TimerTask() {

                @Override
                public void run() {
//                    Log.e(TAG, "ServiceTwo Run: " + System.currentTimeMillis());
                    boolean b = MainActivity.isServiceWorked(ServiceTwo.this, "com.syx.doubleservice.ServiceOne");
                    boolean c=MainActivity.isServiceWorked(ServiceTwo.this,"com.syx.doubleservice.PollingService");
                    if (!b) {
                        Intent service = new Intent(ServiceTwo.this, ServiceOne.class);
                        startService(service);
                        //  PollingUtils.startPollingService(ServiceTwo.this, 20, PollingService.class, PollingService.ACTION);
                    }else if(!c)
                    {
                        PollingUtils.startPollingService(ServiceTwo.this, 2, PollingService.class, PollingService.ACTION);

                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
