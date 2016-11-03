package com.syx.doubleservice;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/19.
 */
public class MessageActivity extends Activity {
    private static final String TAG = "MessageActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MessageActivity.this, "运行了一次了", Toast.LENGTH_SHORT).show();
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        Log.d(TAG, "在message中获取数据的id：+" + GlobalData.pushdataG.get(GlobalData.pushdataG.size() - 1).getID());

    }
}
