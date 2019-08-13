package com.alyndroid.syarty.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;

import com.alyndroid.syarty.data.remote.ApiClient;
import com.alyndroid.syarty.data.remote.ApiInterface;
import com.alyndroid.syarty.data.remote.RxNetworking;
import com.alyndroid.syarty.pojo.SetFireBaseTokenResponse;
import com.alyndroid.syarty.ui.base.BasePresenter;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<LoginView> {
    @Inject
    public LoginPresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }

    @SuppressLint("CheckResult")
    public void login(String phoneNumber, String password) {
        RxNetworking.login(phoneNumber, password)
                .doOnSubscribe(disposable -> getView().setLoading(150))
                .doFinally(() -> getView().setLoaded(150))
                .subscribe(loginResponse -> {
                            getView().onLoginSuccess(loginResponse);
                        }
                        , throwable -> {
                            getView().setLoaded(150);
                            getView().onLoginFail();
                        });
    }

    public void setFireBaseToken(int user_id, String fireBaseToken) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, Object> map = new HashMap();
        map.put("fb_token",fireBaseToken);
        map.put("_method","put");
        Call<SetFireBaseTokenResponse> call = apiInterface.setFireBaseToken(user_id, map);
        call.enqueue(new Callback<SetFireBaseTokenResponse>() {
            @Override
            public void onResponse(Call<SetFireBaseTokenResponse> call, Response<SetFireBaseTokenResponse> response) {
                if (!response.isSuccessful()) {
                    getView().onSetFireBaseTokenFail();

                }
                getView().onSetFireBaseTokenSuccess();
            }

            @Override
            public void onFailure(Call<SetFireBaseTokenResponse> call, Throwable t) {
                getView().onSetFireBaseTokenFail();
            }
        });
    }
}
