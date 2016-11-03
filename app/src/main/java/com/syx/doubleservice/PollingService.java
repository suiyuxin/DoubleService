package com.syx.doubleservice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/7/22.
 */
public class PollingService extends Service {
    String URL = "http://101.200.210.193:7465/";
    Retrofit retrofit;
    RetrofitLinkService ipService;

    public static final String ACTION = "com.syx.doubleservice.PollingService";
    private static final String TAG = "PollingService";

    private int messageNotificationID = 1000;
    private Notification mNotification;
    private NotificationManager mManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initNotifiManager();
    }

//    @Override
//    public void onStart(Intent intent, int startId) {
//        new PollingThread().start();
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        return START_STICKY;
    }

    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.tickerText = "New Message";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    private void showNotification() {
        Notification.Builder builder1 = new Notification.Builder(this);
        builder1.setSmallIcon(R.drawable.ic_launcher); //设置图标
        builder1.setTicker("显示第二个通知");
        builder1.setContentTitle("通知"); //设置标题
        builder1.setContentText("点击查看详细内容"); //消息内容
        builder1.setWhen(System.currentTimeMillis()); //发送时间
        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder1.setAutoCancel(true);//打开程序后图标消失
        Intent intent = new Intent(this, MessageActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder1.setContentIntent(pendingIntent);
        Notification notification1 = builder1.getNotification();
        mManager.notify(messageNotificationID, notification1);
    }

    class PollingThread extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "定时执行一次轮询");
            retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
            ipService = retrofit.create(RetrofitLinkService.class);
            Call<ResponseBody> cl = ipService.GetPullData();
            cl.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Gson gson = new Gson();
                        List<PushDataModel> usesList = gson.fromJson(response.body().string(), new TypeToken<List<PushDataModel>>() {
                        }.getType());
                        if (usesList.isEmpty()) {
                            Log.d(TAG, "userList为空");
                        } else {
                            GlobalData.pushdataG.clear();
                            for (PushDataModel uu : usesList) {
                                GlobalData.pushdataG.add(uu);
                            }
                            SharedPreferences mySharedPreferences = getSharedPreferences("oldId",
                                    Activity.MODE_PRIVATE);
                            String oldId=mySharedPreferences.getString("oldID","");
                            if (GlobalData.pushdataG.get(GlobalData.pushdataG.size() - 1).getID().compareTo(oldId) > 0) {
                                SharedPreferences mySharedPreference = getSharedPreferences("oldId",
                                        Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mySharedPreference.edit();
                                editor.putString("oldID", GlobalData.pushdataG.get(GlobalData.pushdataG.size() - 1).getID());
                                editor.commit();
                                showNotification();
                                messageNotificationID++;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "Failed+" + t.toString());
                }
            });
            try {
                sleep(1000); //暂停一秒
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("PollingService:onDestroy");
    }




}
