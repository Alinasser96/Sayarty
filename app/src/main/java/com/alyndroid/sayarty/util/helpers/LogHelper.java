package com.alyndroid.sayarty.util.helpers;

import android.util.Log;

import com.alyndroid.sayarty.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

public class LogHelper {
    private static final boolean isDebug = BuildConfig.DEBUG;

    public static void info(String tag, String message) {
        if (isDebug) {
            Log.i(tag, message);
        }
    }

    public static void warn(String tag, String message) {
        if (isDebug) {
            Log.w(tag, message);
        }
    }

    public static void error(String tag, String message, Throwable throwable) {
        if (isDebug) {
            Log.e(tag, message, throwable);
        }
    }

    public static void error(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }

    public static void printFirebaseAndLog(String firstParameter, String secondParameter) {
        String printString = firstParameter + " ==> " + secondParameter;
        info("Firebase_Log", printString);

        if (!isDebug) {
            Crashlytics.log(printString);
        }
    }

//    public static void logUser(LoginResponse user) {
//        Crashlytics.setUserIdentifier(user.getIdDriver() + "");
//        Crashlytics.setUserEmail(user.getDriverPhone());
//        Crashlytics.setUserName(user.getDriverFullName());
//    }

    public static void generateFabricAnswers(String actionName) {
        Answers.getInstance().logCustom(new CustomEvent(actionName));
    }
}
