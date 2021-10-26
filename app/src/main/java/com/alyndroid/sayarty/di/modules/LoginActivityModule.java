package com.alyndroid.sayarty.di.modules;

import android.app.Activity;
import android.content.Context;

import javax.inject.Inject;

import dagger.Module;

@Module
public class LoginActivityModule {
    private Context context;

    @Inject
    public LoginActivityModule(Activity context) {
        this.context = context;
    }


}
