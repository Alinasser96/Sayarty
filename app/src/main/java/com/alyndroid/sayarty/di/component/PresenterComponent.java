package com.alyndroid.sayarty.di.component;

import android.content.Context;

import com.alyndroid.sayarty.di.modules.CompositeModule;
import com.alyndroid.sayarty.di.modules.LoginActivityModule;
import com.alyndroid.sayarty.ui.dashboard.DashboardPresenter;
import com.alyndroid.sayarty.ui.login.LoginPresenter;
import com.alyndroid.sayarty.ui.newRequest.NewRequestPresenter;
import com.alyndroid.sayarty.ui.selectDriver.SelectDriverPresenter;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {CompositeModule.class, LoginActivityModule.class})
public interface PresenterComponent {
    LoginPresenter getPresenter();
    DashboardPresenter getDashboardPresenter();
    SelectDriverPresenter getSelectDriverPresenter();
    NewRequestPresenter getNewRequestPresenter();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder acc(Context context);

        PresenterComponent build();
    }
}
