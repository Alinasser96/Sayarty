package com.alyndroid.syarty.ui.newRequest;

import android.content.Context;

import com.alyndroid.syarty.data.remote.ApiClient;
import com.alyndroid.syarty.data.remote.ApiInterface;
import com.alyndroid.syarty.pojo.SetFireBaseTokenResponse;
import com.alyndroid.syarty.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRequestPresenter extends BasePresenter<NewRequestView> {
    @Inject
    public NewRequestPresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }

    public void sendStatus(int status, int operation_id, String photosID, ArrayList attached) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, Object> map = new HashMap();
        map.put("_method", "put");
        map.put("status", status);
        map.put("photo_receiver_id", photosID);
        map.put("attachment_car", attached);
        Call<SetFireBaseTokenResponse> call = apiInterface.setOperationStatus(operation_id, map);
        getView().setLoading(150);
        call.enqueue(new Callback<SetFireBaseTokenResponse>() {
            @Override
            public void onResponse(Call<SetFireBaseTokenResponse> call, Response<SetFireBaseTokenResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onSendStatusFail();

                }
                getView().onSendStatusSuccess();
            }

            @Override
            public void onFailure(Call<SetFireBaseTokenResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onSendStatusFail();
            }
        });
    }

    public void sendStatus(int status, int operation_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, Object> map = new HashMap();
        map.put("_method", "put");
        map.put("status", status);
        map.put("photo_receiver_id", "");
        Call<SetFireBaseTokenResponse> call = apiInterface.setOperationStatus(operation_id, map);
        getView().setLoading(150);
        call.enqueue(new Callback<SetFireBaseTokenResponse>() {
            @Override
            public void onResponse(Call<SetFireBaseTokenResponse> call, Response<SetFireBaseTokenResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onSendStatusFail();

                }
                getView().onSendStatusSuccess();
            }

            @Override
            public void onFailure(Call<SetFireBaseTokenResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onSendStatusFail();
            }
        });
    }

    public void sendStatus(int status, String delay, int operation_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, Object> map = new HashMap();
        map.put("_method", "put");
        map.put("status", status);
        map.put("photo_receiver_id", "");
        map.put("delay", delay);
        Call<SetFireBaseTokenResponse> call = apiInterface.setOperationStatus(operation_id, map);
        getView().setLoading(150);
        call.enqueue(new Callback<SetFireBaseTokenResponse>() {
            @Override
            public void onResponse(Call<SetFireBaseTokenResponse> call, Response<SetFireBaseTokenResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onSendStatusFail();
                }
                getView().onSendStatusSuccess();
            }

            @Override
            public void onFailure(Call<SetFireBaseTokenResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onSendStatusFail();
            }
        });
    }

    public void setVisible(int operation_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, Object> map = new HashMap();
        map.put("_method", "put");
        map.put("request_received", 1);
        Call<SetFireBaseTokenResponse> call = apiInterface.setIsVisible(operation_id, map);
        getView().setLoading(151);
        call.enqueue(new Callback<SetFireBaseTokenResponse>() {
            @Override
            public void onResponse(Call<SetFireBaseTokenResponse> call, Response<SetFireBaseTokenResponse> response) {
                //onResponse
            }

            @Override
            public void onFailure(Call<SetFireBaseTokenResponse> call, Throwable t) {
                //onFail
            }
        });
    }


}
