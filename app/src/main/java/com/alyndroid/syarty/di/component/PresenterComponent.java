package com.alyndroid.syarty.di.component;

import android.content.Context;

import com.alyndroid.syarty.di.modules.CompositeModule;
import com.alyndroid.syarty.di.modules.LoginActivityModule;
import com.alyndroid.syarty.ui.dashboard.DashboardPresenter;
import com.alyndroid.syarty.ui.dashboard.DashboardView;
import com.alyndroid.syarty.ui.login.LoginPresenter;
import com.alyndroid.syarty.ui.newRequest.NewRequestPresenter;
import com.alyndroid.syarty.ui.selectDriver.SelectDriverPresenter;

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
