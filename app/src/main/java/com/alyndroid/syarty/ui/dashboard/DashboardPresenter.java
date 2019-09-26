package com.alyndroid.syarty.ui.dashboard;

import android.content.Context;

import com.alyndroid.syarty.data.remote.ApiClient;
import com.alyndroid.syarty.data.remote.ApiInterface;
import com.alyndroid.syarty.pojo.AllDriversResponse;
import com.alyndroid.syarty.pojo.CarsResponse;
import com.alyndroid.syarty.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPresenter extends BasePresenter<DashboardView> {
    @Inject
    public DashboardPresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }
    public void getCars(int user_id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CarsResponse> call = apiInterface.getCars(user_id);
        getView().setLoading(150);
        call.enqueue(new Callback<CarsResponse>() {
            @Override
            public void onResponse(Call<CarsResponse> call, Response<CarsResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onGetCarsFail(String.valueOf(response.code()));
                }

                if (response.body() != null) {
                    getView().onGetCarsSuccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<CarsResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onGetCarsFail(t.getMessage());
            }
        });
    }
}
