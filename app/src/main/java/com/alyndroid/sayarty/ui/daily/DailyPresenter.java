package com.alyndroid.sayarty.ui.daily;

import android.content.Context;

import com.alyndroid.sayarty.data.remote.ApiClient;
import com.alyndroid.sayarty.data.remote.ApiInterface;
import com.alyndroid.sayarty.data.remote.RxNetworking;
import com.alyndroid.sayarty.pojo.Comments;
import com.alyndroid.sayarty.pojo.StoreResponse;
import com.alyndroid.sayarty.ui.base.BasePresenter;

import java.io.File;
import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyPresenter extends BasePresenter<DailyView> {
    public DailyPresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }

    public void store(File rightPhotoFile, File backPhotoFile, File leftPhotoFile, File frontPhotoFile, Comments comments, int user_id, int car_ID, int status) {
        RxNetworking.store( rightPhotoFile, backPhotoFile, leftPhotoFile, frontPhotoFile, comments, user_id, car_ID, status)
                .doOnSubscribe(disposable -> getView().setLoading(150))
                .doFinally(() -> getView().setLoaded(150))
                .subscribe(storeResponse -> {
                            getView().onStoreSuccess(storeResponse);
                        }
                        , throwable -> {
                            getView().setLoaded(150);
                            getView().onStoreFail(throwable.getMessage());
                        });
    }

    public void sendToReceiver(HashMap map){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StoreResponse> call = apiInterface.sendToReceiver(map);
        getView().setLoading(150);
        call.enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onSendToReceiverFail(response.code()+"");
                }

                getView().onSendToReceiverSuccess();
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onSendToReceiverFail(t.getMessage());
            }
        });
    }

}
