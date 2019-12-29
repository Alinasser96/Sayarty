package com.alyndroid.syarty.ui.splash;

import android.content.Context;

import com.alyndroid.syarty.data.remote.RxNetworking;
import com.alyndroid.syarty.ui.base.BasePresenter;

import org.reactivestreams.Subscriber;

import java.nio.BufferOverflowException;

import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
