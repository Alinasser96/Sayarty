package com.alyndroid.syarty.ui.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.event.NetworkAvailabilityEvent;
import com.alyndroid.syarty.ui.login.LoginActivity;
import com.alyndroid.syarty.util.CommonUtils;
import com.alyndroid.syarty.util.Connection;
import com.alyndroid.syarty.util.constants.ConstantPermissions;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

import butterknife.Unbinder;
import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;
    private ProgressDialog progressDialog;
    private NetworkInfo.State prevState = NetworkInfo.State.CONNECTED;
    private AlertDialog internetAlertDialog;
    private AlertDialog gpsAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.text_please_wait));

        initializeDialogs();
//        if (isConnected())
//            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//                SharedPreferenceHelper.getInstance(this).saveFirebaseToken(task.getResult().getToken());
//            });
    }

    private void setArabicLayoutDirection() {
        Locale locale = new Locale("ar");

        Configuration configuration = getResources().getConfiguration();
        Locale.setDefault(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            configuration.setLayoutDirection(new Locale("ar"));
            createConfigurationContext(configuration);
        }
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    private void initializeDialogs() {
        internetAlertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.no_internet))
                .setMessage(getString(R.string.no_internet_message))
                .setPositiveButton(getString(R.string.enable_3g), (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivityForResult(intent, ConstantPermissions.INTERNET_SETTINGS_DIALOG);
                }).setNegativeButton(getString(R.string.enable_wifi),
                        (dialog, which) -> startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),
                                ConstantPermissions.INTERNET_SETTINGS_DIALOG))
                .setNeutralButton(getString(R.string.exit),
                        (dialog, which) -> finish())
                .create();

        gpsAlertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.no_gps))
                .setMessage(getString(R.string.no_gps_message))
                .setPositiveButton(getString(R.string.enable_gps), (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, ConstantPermissions.GPS_SETTINGS_DIALOG);
                })
                .setNegativeButton(getString(R.string.exit), (dialog, which) -> finish())
                .create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setArabicLayoutDirection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(NetworkAvailabilityEvent networkAvailabilityEvent) {
        handleInternetAvailabilityChange();
    }


    private void handleInternetAvailabilityChange() {
        if (isConnected()) {
            hideInternetDialog();
            onResume();
        } else {
            showInternetDialog();
        }
    }


    public final void showInternetDialog() {
        runOnUiThread(() -> {
            if (!(this).isFinishing() && !internetAlertDialog.isShowing()) {
                internetAlertDialog.show();
            }
        });
    }

    public final void hideInternetDialog() {
        runOnUiThread(() -> {
            if (!(this).isFinishing() && internetAlertDialog.isShowing()) {
                internetAlertDialog.dismiss();
            }
        });
    }

    public final void showGPSDialog() {
        runOnUiThread(() -> {
            if (!(this).isFinishing() && !gpsAlertDialog.isShowing()) {
                gpsAlertDialog.show();
            }
        });
    }

    public final void hideGPSDialog() {
        runOnUiThread(() -> {
            if (!(this).isFinishing() && gpsAlertDialog.isShowing()) {
                gpsAlertDialog.dismiss();
            }
        });
    }

    protected boolean isDataValid(EditText... data) {
        for (EditText checkET : data) {
            if (TextUtils.isEmpty(checkET.getText().toString().trim())) {
                checkET.setError(getString(R.string.empty_edit_text_error_message));
                checkET.setFocusableInTouchMode(true);
                checkET.requestFocus();
                return false;
            } else {
                checkET.setError(null);
            }
        }
        return true;
    }

    protected boolean isDataValid(TextView... data) {
        for (TextView checkET : data) {
            if (TextUtils.isEmpty(checkET.getText().toString().trim())) {
                checkET.setError(getString(R.string.empty_edit_text_error_message));
                checkET.setFocusableInTouchMode(true);
                checkET.requestFocus();
                return false;
            } else {
                checkET.setError(null);
            }
        }
        return true;
    }

    protected void clearFields(EditText... data) {
        for (EditText editText : data) {
            editText.getText().clear();
        }
    }

    protected void showLoadingProgress(String title, String message) {
        mProgressDialog = CommonUtils.showLoadingDialog(this, title, message);
    }

    protected void hideLoadingProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void enableDisableRoot(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableRoot(group.getChildAt(idx), enabled);
            }
        }
    }

    public void showToast(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void showViews(View... views) {
        for (View v : views)
            if (v != null)
                v.setVisibility(View.VISIBLE);
    }

    public void hideViews(View... views) {
        for (View v : views)
            if (v != null)
                v.setVisibility(View.GONE);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null)
            mUnBinder.unbind();
        super.onDestroy();
    }


    protected void showTextInputLayoutError(TextInputLayout textInputEditText, String errorMessage) {
        textInputEditText.setError(errorMessage);
    }

    protected void removeTextInputLayoutError(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
    }

    protected void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void showProgress(final String message) {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(() -> {
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        });
    }

    public void hideProgress() {
        if (isFinishing()) {
            return;
        }
        runOnUiThread(() -> {
            if (null != progressDialog && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
    }

    public void logoutAction(Context context) {

        //clear cache
        SharedPreferenceHelper.getInstance(this).logoutAction();

        //goto login
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        try {
            ActivityCompat.finishAffinity(this);
        } catch (IllegalStateException e) {
            finish();
        }
    }

    public boolean isConnected() {
        return Connection.isNetworkAvailable(this);
    }
}

