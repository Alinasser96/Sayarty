package com.alyndroid.sayarty.ui.splash;

import com.alyndroid.sayarty.pojo.CheckVersionResponse;
import com.alyndroid.sayarty.ui.base.BaseView;

public interface SplashView extends BaseView {
    void onCheckVersionSuccess(CheckVersionResponse checkVersionResponse);
    void onCheckVersionFail();
}
