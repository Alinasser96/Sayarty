package com.alyndroid.syarty.util;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alyndroid.syarty.util.helpers.LogHelper;

public class Connection {

    private static String TAG = Connection.class.getSimpleName();

    public static boolean isGPSAvailable(Context activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isInternetAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            LogHelper.info(TAG, "null connectivity");
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info != null) {
                switch (info.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                    case ConnectivityManager.TYPE_MOBILE:
                        return true;

                    default:
//                        showSlowInternetConnectionToast(activity);
                        return false;
                }
            } else {
//                showSlowInternetConnectionToast(activity);
            }
        }
        return false;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            LogHelper.info(TAG, "null connectivity");
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info != null) {
                switch (info.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                    case ConnectivityManager.TYPE_MOBILE:
                        return true;

                    default:
//                        showSlowInternetConnectionToast(activity);
                        return false;
                }
            } else {
//                showSlowInternetConnectionToast(activity);
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
