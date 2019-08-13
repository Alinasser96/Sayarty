package com.alyndroid.syarty.util;

public interface Constant {

    String DEVICE_TYPE = "Android";

    interface FacebookPermissions {
        String publicProfilePermission = "public_profile";
        String emailPermission = "email";
    }



    interface Api {
        interface Url {
            String BASE_URL = "http://car.waritex.org/public//api/";  //local
//            String BASE_URL = "";  //staging

            String TOKEN = "Token";

            String login = BASE_URL + "login";
            String checkVersion = BASE_URL + "check-version";
            String store = BASE_URL + "photos";
        }

        interface CheckAppVersion {
            String DEVICE_TYPE = "DeviceType";
            String APP_VERSION = "AppVersion";
        }



        interface ERROR_CODES {
        }
    }

    interface INTENT_EXTRAS {
        String right_Side ="RIGHT_SIDE";
        String back_side = "BACK_SIDE";
        String left_side = "LEFT_SIDE";
        String front_side = "FRONT_SIDE";
        String receiverID = "receiverID";
        String isFrom = "isFrom";
        String NewRequest = "NewRequest";
        String SelectReceiver = "SelectReceiver";
        String NewCase = "NewCase";
        String Operation_id = "Operation_id";
        String SIDE_NUMBER = "SIDE_NUMBER";
        String EDITED_PHOTO = "EDITED_PHOTO";
        String COMMENTS = "Comments";
        String RECEIVER_PHONE = "RECEIVER_PHONE";
        String CAR_ID = "car_id";
        String FREQ_REMINDER = "FREQ_REMINDER ";
        String ATTACHED = "ATTACHED";
    }

    interface REQUEST_CODES {
    }

    interface Util {
        boolean DISABLE = false;
        boolean ENABLE = true;
    }

    interface SharedPreference {
        String USER_TAG = "user_tag";
        String ACCOUNT_AUTHORIZATION = "account_authorization";
        String FIREBASE_TOKEN = "firebase_token";
        String ID_TRIP = "id_trip";
        String CURRENT_STATION_INDEX = "current_station_index";
    }

    interface PushNotificationsIds {
        int DEFAULT_NOTIFICATION = 999;
        int NEW_REQUEST = 1;
        int CANCEL_REQUEST = 2;
        int CONTACT_US = 3;
        int NEW_NOTIFICATION = 5;
        int LOGOUT_ACTION = 21;
        int UPDATE_LOCATIONS_SERVICE = 10;
        int UPDATE_LOCATIONS_SERVICE_OREO = 11;
        int NO_UNIQUE_ID_PUSH_NOTIFICATION = -1;
    }

    interface Action {
        String UPDATE_LOCATION = "UPDATE_LOCATION";
        String TRIP_UPDATE_LOCATION = "TRIP_UPDATE_LOCATION";

        String CHANNEL_NAME = "GORide Pilot";
    }
    interface permission{
        int STORAGE = 1;
        int right_side_IMAGE = 100;
        int back_side_IMAGE = 101;
        int left_side_IMAGE = 102;
        int front_side_IMAGE = 103;

    }
}
