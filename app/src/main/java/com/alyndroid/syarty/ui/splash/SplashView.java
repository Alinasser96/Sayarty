package com.alyndroid.syarty.ui.splash;

import com.alyndroid.syarty.pojo.CheckVersionResponse;
import com.alyndroid.syarty.ui.base.BaseView;

public interface SplashView extends BaseView {
    void onCheckVersionSuccess(CheckVersionResponse checkVersionResponse);
    void onCheckVersionFail();
}
