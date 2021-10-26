package com.alyndroid.sayarty.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.alyndroid.sayarty.data.local.SharedPreferenceHelper;
import com.androidnetworking.AndroidNetworking;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SyartyApp  extends Application {
    private static PackageInfo packageInfo;
    private static Context context;
    public static final String CHANNEL_ID = "SyartyChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();

        AndroidNetworking.initialize(getApplicationContext());
        SharedPreferenceHelper.init(this);
        createNotificationChannel();

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //disable firebase while debug
//        Fabric.with(this, new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "delay service channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return context;
    }

    public static class Device {

        public static final String MANUFACTURE;
        public static final String MODEL;

        static {
            MANUFACTURE = Build.MANUFACTURER;
            MODEL = Build.MODEL;
        }

        public static class OS {

            public static final String TYPE;
            public static final String VERSION;

            static {
                TYPE = "Android";
                VERSION = Build.VERSION.RELEASE;
            }
        }
    }

    public static class Application {

        public static final int VERSION_CODE;
        public static final String VERSION_NAME;

        static {
            VERSION_CODE = packageInfo.versionCode;
            VERSION_NAME = packageInfo.versionName;
        }
    }
}
