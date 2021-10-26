package com.alyndroid.sayarty.data.remote;

import com.alyndroid.sayarty.pojo.CheckVersionResponse;
import com.alyndroid.sayarty.pojo.Comments;
import com.alyndroid.sayarty.pojo.LoginResponse;
import com.alyndroid.sayarty.pojo.StoreResponse;
import com.alyndroid.sayarty.util.Constant;
import com.alyndroid.sayarty.util.RxUtils;
import com.alyndroid.sayarty.util.constants.ConstantValues;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;

public class RxNetworking {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            //.addInterceptor(new ChuckInterceptor(SyartyApp.getAppContext()))
            .build();

    public static Observable<CheckVersionResponse> checkVersion() {
        return Rx2AndroidNetworking.get(Constant.Api.Url.checkVersion)
                .setOkHttpClient(client)
                .build()
                .getObjectObservable(CheckVersionResponse.class)
                .compose(RxUtils.applyNetworkSchedulers());
    }

    public static Observable<LoginResponse> login(String phoneNumber, String password) {
        return Rx2AndroidNetworking.post(Constant.Api.Url.login)
                .addBodyParameter(ConstantValues.PHONE, phoneNumber)
                .addBodyParameter(ConstantValues.PASSWORD, password)
                .setOkHttpClient(client)
                .build()
                .getObjectObservable(LoginResponse.class)
                .compose(RxUtils.applyNetworkSchedulers());
    }


    public static Observable<StoreResponse> store(File rightPhotoFile, File backPhotoFile, File leftPhotoFile, File frontPhotoFile, Comments comments, int user_ID, int car_ID, int status) {
        return Rx2AndroidNetworking.upload(Constant.Api.Url.store)
                .addMultipartFile(ConstantValues.right_SIDE_IMG, rightPhotoFile)
                .addMultipartFile(ConstantValues.back_SIDE_IMG, backPhotoFile)
                .addMultipartFile(ConstantValues.LEFT_SIDE_IMG, leftPhotoFile)
                .addMultipartFile(ConstantValues.front_SIDE_IMG, frontPhotoFile)
                .addMultipartParameter(ConstantValues.right_SIDE_NOTE, comments.getRightSideComment())
                .addMultipartParameter(ConstantValues.BACK_SIDE_NOTE, comments.getBackSideComment())
                .addMultipartParameter(ConstantValues.LEFT_SIDE_NOTE, comments.getLeftSideComment())
                .addMultipartParameter(ConstantValues.FRONT_SIDE_NOTE, comments.getFrontSideComment())
                .addMultipartParameter(ConstantValues.GENERAL_COMMENT, comments.getGeneralComment())
                .addMultipartParameter(ConstantValues.USER_ID, String.valueOf(user_ID))
                .addMultipartParameter(ConstantValues.CAR_ID, String.valueOf(car_ID))
                .addMultipartParameter(ConstantValues.STATUS, String.valueOf(status))
                .setOkHttpClient(client)
                .build()
                .getObjectObservable(StoreResponse.class)
                .compose(RxUtils.applyNetworkSchedulers());
    }

}
