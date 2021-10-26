package com.alyndroid.sayarty.ui.login;

import com.alyndroid.sayarty.pojo.LoginResponse;
import com.alyndroid.sayarty.ui.base.BaseView;

public interface LoginView extends BaseView {
    void onLoginSuccess(LoginResponse loginResponse);
    void onLoginFail();

    void onSetFireBaseTokenSuccess();
    void onSetFireBaseTokenFail();
}
