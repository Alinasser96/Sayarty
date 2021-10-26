package com.alyndroid.sayarty.ui.newIssue;

import android.content.Context;

import com.alyndroid.sayarty.data.remote.ApiClient;
import com.alyndroid.sayarty.data.remote.ApiInterface;
import com.alyndroid.sayarty.pojo.SetFireBaseTokenResponse;
import com.alyndroid.sayarty.ui.base.BasePresenter;

import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewIssuePresenter extends BasePresenter<NewIssueView> {
    public NewIssuePresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }

    public void sendIssue(HashMap map){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SetFireBaseTokenResponse> call = apiInterface.sendIssue(map);
        getView().setLoading(150);
        call.enqueue(new Callback<SetFireBaseTokenResponse>() {
            @Override
            public void onResponse(Call<SetFireBaseTokenResponse> call, Response<SetFireBaseTokenResponse> response) {
                getView().setLoaded(150);
                if (!response.isSuccessful()) {
                    getView().onSendIssueFail();
                }

                getView().onSendIssueSuccess();
            }

            @Override
            public void onFailure(Call<SetFireBaseTokenResponse> call, Throwable t) {
                getView().setLoaded(150);
                getView().onSendIssueFail();
            }
        });
    }
}
