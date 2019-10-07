package com.alyndroid.syarty.ui.splash;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alyndroid.syarty.BuildConfig;
import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.di.component.DaggerPresenterComponent;
import com.alyndroid.syarty.di.component.PresenterComponent;
import com.alyndroid.syarty.pojo.CheckVersionResponse;
import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.ui.dashboard.DashboardActivity;
import com.alyndroid.syarty.ui.login.LoginActivity;
import com.alyndroid.syarty.ui.login.LoginPresenter;
import com.alyndroid.syarty.ui.login.LoginView;
import com.alyndroid.syarty.util.helpers.LogHelper;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class SplashActivity extends BaseActivity implements SplashView, LoginView {

    private static final String TAG = "SplashActivity";
    @BindView(R.id.logo_imageView)
    ImageView logoImageView;
    @BindView(R.id.app_name_textView)
    TextView appNameTextView;
    int versionCode = BuildConfig.VERSION_CODE;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SplashPresenter splashPresenter;
    private CoreUserData user;
    private LoginPresenter loginPresenter;
    private SharedPreferenceHelper preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        splashPresenter = new SplashPresenter(compositeDisposable, this);
        splashPresenter.attachView(this);
        splashPresenter.checkVersion();

        preferenceHelper = new SharedPreferenceHelper();
        user = new CoreUserData();

        if (TextUtils.isEmpty(preferenceHelper.getFirebaseToken())) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            LogHelper.info(TAG, "Refreshed token: " + refreshedToken);
            SharedPreferenceHelper.getInstance(this).saveFirebaseToken(refreshedToken);
        }

    }

    @Override
    public void onCheckVersionSuccess(CheckVersionResponse checkVersionResponse) {

        if (checkVersionResponse.getData().get(0).getCode()> versionCode) {
            showUpdateVersionDialog();
        } else {
            if (SharedPreferenceHelper.getInstance(this).getCoreUserData() != null) {
                user = SharedPreferenceHelper.getInstance(this).getCoreUserData();
                PresenterComponent component = DaggerPresenterComponent
                        .builder()
                        .acc(this)
                        .build();
                loginPresenter = component.getPresenter();
                loginPresenter.attachView(this);
                loginPresenter.login(user.getPhone(), user.getPassword());
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }

    }

    @Override
    public void onCheckVersionFail() {
        splashPresenter.checkVersion();
    }

    @Override
    public void setLoading(int apiCode) {

    }

    @Override
    public void setLoaded(int apiCode) {

    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {


        user.setUserId(Integer.parseInt(loginResponse.getData().getUserId()));

        SharedPreferenceHelper.getInstance(this).saveCoreUserData(user, false);

        loginPresenter.setFireBaseToken(user.getUserId(), SharedPreferenceHelper.getInstance(this).getFirebaseToken());

    }

    @Override
    public void onLoginFail() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onSetFireBaseTokenSuccess() {
        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
        finish();
    }

    @Override
    public void onSetFireBaseTokenFail() {
        loginPresenter.setFireBaseToken(user.getUserId(), SharedPreferenceHelper.getInstance(this).getFirebaseToken());
    }

    protected void showUpdateVersionDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.update_version)
                .setMessage(R.string.update_version_available)
                .setPositiveButton(R.string.update, (dialogInterface, i) -> {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }).create().show();
    }
}
