package com.alyndroid.syarty.ui.login;

import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseView;

public interface LoginView extends BaseView {
    void onLoginSuccess(LoginResponse loginResponse);
    void onLoginFail();

    void onSetFireBaseTokenSuccess();
    void onSetFireBaseTokenFail();
}
