package com.alyndroid.sayarty.ui.splash;

import android.content.Context;

import com.alyndroid.sayarty.data.remote.RxNetworking;
import com.alyndroid.sayarty.ui.base.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;

public class SplashPresenter extends BasePresenter<SplashView> {
    public SplashPresenter(CompositeDisposable compositeDisposable, Context context) {
        super(compositeDisposable, context);
    }
    public void checkVersion(){
        RxNetworking.checkVersion()
                .doOnSubscribe(disposable -> getView().setLoading(150))
                .doFinally(() -> getView().setLoaded(150))
                .subscribe(checkVersionResponse -> {
                            getView().onCheckVersionSuccess(checkVersionResponse);
                        }
                        , throwable -> {
                            getView().setLoaded(150);
                            getView().onCheckVersionFail();
                        });
    }

}
