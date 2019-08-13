package com.alyndroid.syarty.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.util.Constant;
import com.google.gson.Gson;

import javax.inject.Inject;

public class SharedPreferenceHelper {


    private static SharedPreferenceHelper mSharedPreferenceHelper;
    private static SharedPreferences mSharedPreferences;
    private static Context context;
    private final String DEVICE_TOKEN = "device_token";
    private CoreUserData coreUserData;

    @Inject
    public SharedPreferenceHelper() {
    }

    public static void init(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        SharedPreferenceHelper.context = context;
        if (mSharedPreferenceHelper == null) {
            synchronized (SharedPreferenceHelper.class) {
                if (mSharedPreferenceHelper == null) {
                    mSharedPreferenceHelper = new SharedPreferenceHelper();
                }
            }
        }
        return mSharedPreferenceHelper;
    }


    public boolean saveCoreUserData(CoreUserData response, boolean uiThread) {
        if (response == null) {
            return false;
        }
        String userString = new Gson().toJson(response);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Constant.SharedPreference.USER_TAG, userString);
        if (uiThread) {
            editor.apply();
            return true;
        } else {
            if (editor.commit()) {
                coreUserData = response;
                return true;
            } else {
                return false;
            }
        }
    }

    public CoreUserData getCoreUserData() {
        if (coreUserData == null) {
            synchronized (CoreUserData.class) {
                if (coreUserData == null) {
                    String userString = mSharedPreferences.getString(Constant.SharedPreference.USER_TAG, null);
                    if (userString != null) {
                        coreUserData = null;
                        coreUserData = new Gson().fromJson(userString, CoreUserData.class);

                    } else
                        return null;
                }
            }
        }

        return coreUserData;
    }

    public void saveAccountAuthorization(String accountAuthorization) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Constant.SharedPreference.ACCOUNT_AUTHORIZATION, accountAuthorization);
        editor.apply();
    }

    public String getAccountAuthorization() {
        return mSharedPreferences.getString(Constant.SharedPreference.ACCOUNT_AUTHORIZATION, null);
    }

    public void saveFirebaseToken(String token) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Constant.SharedPreference.FIREBASE_TOKEN, token);
        editor.apply();
    }

    public String getFirebaseToken() {
        return mSharedPreferences.getString(Constant.SharedPreference.FIREBASE_TOKEN, "");
    }
}
