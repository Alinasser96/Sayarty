package com.alyndroid.syarty.fcm;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.ui.newRequest.NewRequestActivity;
import com.alyndroid.syarty.ui.refused.RefuseActivity;
import com.alyndroid.syarty.util.Constant;
import com.alyndroid.syarty.util.helpers.LogHelper;
import com.alyndroid.syarty.util.helpers.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ServiceFirebaseMessaging extends FirebaseMessagingService {
    protected String TAG;

    {
        TAG = getClass().getSimpleName();
    }

    private NotificationHelper notificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new NotificationHelper(this);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        LogHelper.info(TAG, "Refreshed token: " + token);
        SharedPreferenceHelper.getInstance(this).saveFirebaseToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        switch (remoteMessage.getData().get("message")) {
            case "1":
                Intent pushIntent = new Intent("NewRequest");
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushIntent);
                Intent intent = new Intent(ServiceFirebaseMessaging.this, NewRequestActivity.class);
                intent.putExtra(Constant.INTENT_EXTRAS.Operation_id, remoteMessage.getData().get("operation_id"));
                intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, remoteMessage.getData().get("car_id"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case "2":
                notificationHelper.generateNotification(getString(R.string.refused), getString(R.string.receiver_refused));
                Intent refusedIntent = new Intent(ServiceFirebaseMessaging.this, RefuseActivity.class);
                refusedIntent.putExtra(Constant.INTENT_EXTRAS.RECEIVER_PHONE, remoteMessage.getData().get("phone"));
                refusedIntent.putExtra(Constant.INTENT_EXTRAS.RECEIVER_NAME, remoteMessage.getData().get("driver_name"));
                refusedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(refusedIntent);
                break;
            case "3":
                List<String> carIds = convertJsonTolList(remoteMessage.getData().get("car_id"));
                Intent pushIntent2 = new Intent("frequent");
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushIntent2);
                Intent intent2 = new Intent(ServiceFirebaseMessaging.this, NewRequestActivity.class);
                //todo more one id
                intent2.putExtra(Constant.INTENT_EXTRAS.CAR_ID, carIds.get(0));
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;


        }
    }



    private List<String> convertJsonTolList(String data){
        List<String> list = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = (JSONObject) array.get(i);
                list.add(((JSONObject) array.get(i)).get("id").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
