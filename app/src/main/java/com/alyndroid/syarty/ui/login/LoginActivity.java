package com.alyndroid.syarty.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.di.component.DaggerPresenterComponent;
import com.alyndroid.syarty.di.component.PresenterComponent;
import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.ui.dashboard.DashboardActivity;
import com.alyndroid.syarty.util.CommonUtils;
import com.alyndroid.syarty.util.constants.ConstantRegex;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {

    @BindView(R.id.phone_number_et)
    EditText phoneNumberEt;
    @BindView(R.id.phone_number_text_input_layout)
    TextInputLayout phoneNumberTextInputLayout;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.password_text_input_layout)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.login_tv)
    Button loginTv;
    private ProgressDialog progressDialog;
    private LoginPresenter loginPresenter;
    private String phoneNumber;
    private String password;
    CoreUserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        //progressDialog initialization
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("wait..");

        //presenter initialization
        PresenterComponent component = DaggerPresenterComponent.builder()
                .acc(this)
                .build();
        loginPresenter = component.getPresenter();
        loginPresenter.attachView(this);

        //buttons initialization
        loginTv.setOnClickListener(this);

        user = new CoreUserData();
    }

    public boolean isValid() {

        removeErrorMessages();

        phoneNumber = phoneNumberEt.getText().toString().trim();
        password = passwordEt.getText().toString();

        View currentView = null;

        if (TextUtils.isEmpty(password)) {
            currentView = passwordEt;
            showTextInputLayoutError(passwordTextInputLayout, getString(R.string.password_error));
        } else if (!CommonUtils.isValidRegex(password, ConstantRegex.PASSWORD)) {
            currentView = passwordEt;
            showTextInputLayoutError(passwordTextInputLayout, getString(R.string.password_error));
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            currentView = phoneNumberEt;
            showTextInputLayoutError(phoneNumberTextInputLayout, getString(R.string.phone_number_error));
        } else if (!CommonUtils.isValidRegex(phoneNumber, ConstantRegex.MOBILE_NUMBER)) {
            currentView = phoneNumberEt;
            showTextInputLayoutError(phoneNumberTextInputLayout, getString(R.string.phone_number_error));
        }

        if (null != currentView) {
            requestFocus(currentView);
        }

        return (null == currentView);
    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {

        user.setName(loginResponse.getData().getUsername());
        user.setPassword(password);
        user.setPhone(phoneNumber);
//        if (loginResponse.getData().getCarInfo().get(0)!= null){
        user.setUserId(Integer.parseInt(loginResponse.getData().getUserId()));


        SharedPreferenceHelper.getInstance(this).saveCoreUserData(user, false);

        loginPresenter.setFireBaseToken(user.getUserId(), SharedPreferenceHelper.getInstance(this).getFirebaseToken());

    }

    @Override
    public void onLoginFail() {
        CommonUtils.showToast(this, getString(R.string.login_error));
    }

    @Override
    public void onSetFireBaseTokenSuccess() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    @Override
    public void onSetFireBaseTokenFail() {
        loginPresenter.setFireBaseToken(user.getUserId(), SharedPreferenceHelper.getInstance(this).getFirebaseToken());
    }

    @Override
    public void setLoading(int apiCode) {
        showProgress(getString(R.string.loading));
    }

    @Override
    public void setLoaded(int apiCode) {
        hideProgress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv:
                if (isValid()) {
                    loginPresenter.login(phoneNumberEt.getText().toString(),
                            passwordEt.getText().toString());
                }
        }
    }

    private void removeErrorMessages() {
        removeTextInputLayoutError(phoneNumberTextInputLayout);
        removeTextInputLayoutError(passwordTextInputLayout);
    }
}
