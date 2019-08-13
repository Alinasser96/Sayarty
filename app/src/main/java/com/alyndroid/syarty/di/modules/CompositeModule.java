package com.alyndroid.syarty.di.modules;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class CompositeModule {

    @Provides
    static CompositeDisposable compositeDisposableProvider() {
        return new CompositeDisposable();
    }
}
