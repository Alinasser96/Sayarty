package com.alyndroid.sayarty.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.alyndroid.sayarty.R;
import com.alyndroid.sayarty.ui.newRequest.NewRequestActivity;
import com.alyndroid.sayarty.ui.splash.SplashActivity;
import com.alyndroid.sayarty.util.Constant;

import static com.alyndroid.sayarty.application.SyartyApp.CHANNEL_ID;

public class DelayService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.delay))
                .setContentText(getString(R.string.delay_message))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.delay_message)))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(0, notification);


        try {
            Thread.sleep(30000);
            Intent intent1 = new Intent(this, NewRequestActivity.class);
            intent1.putExtra(Constant.INTENT_EXTRAS.Operation_id, intent.getStringExtra(Constant.INTENT_EXTRAS.Operation_id));
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            stopSelf();
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }


        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
