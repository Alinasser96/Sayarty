package com.alyndroid.sayarty.ui.selectDriver;

import android.content.Context;

import com.alyndroid.sayarty.data.remote.ApiClient;
import com.alyndroid.sayarty.data.remote.ApiInterface;
import com.alyndroid.sayarty.pojo.AllDriversResponse;
import com.alyndroid.sayarty.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectDriverPresenter extends BasePresenter<SelectDriverView> {

    @Inject
    public SelectDriverPresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }

    public void getDrivers() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AllDriversResponse> call = apiInterface.getDrivers();
        getView().setLoading(150);
        call.enqueue(new Callback<AllDriversResponse>() {
            @Override
            public void onResponse(Call<AllDriversResponse> call, Response<AllDriversResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onGetDriversFail(String.valueOf(response.code()));
                }

                getView().onGetDriversSuccess(response.body().getData());
            }

            @Override
            public void onFailure(Call<AllDriversResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onGetDriversFail(t.getMessage());
            }
        });
    }

}
