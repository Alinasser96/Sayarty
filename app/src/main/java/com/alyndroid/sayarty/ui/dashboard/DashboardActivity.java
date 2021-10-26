package com.alyndroid.sayarty.ui.dashboard;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.sayarty.R;
import com.alyndroid.sayarty.data.local.SharedPreferenceHelper;
import com.alyndroid.sayarty.di.component.DaggerPresenterComponent;
import com.alyndroid.sayarty.di.component.PresenterComponent;
import com.alyndroid.sayarty.pojo.CoreUserData;
import com.alyndroid.sayarty.pojo.LoginResponse;
import com.alyndroid.sayarty.ui.base.BaseActivity;
import com.alyndroid.sayarty.ui.daily.CaptureActivity;
import com.alyndroid.sayarty.ui.newIssue.NewIssueActivity;
import com.alyndroid.sayarty.ui.selectCar.SelectCarActivity;
import com.alyndroid.sayarty.ui.selectDriver.SelectDriverActivity;
import com.alyndroid.sayarty.util.Constant;
import com.alyndroid.sayarty.util.helpers.ShakeListener;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseActivity implements View.OnClickListener, DashboardView {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.daily_pics)
    LinearLayout dailyPics;
    @BindView(R.id.tasleem)
    LinearLayout tasleem;
    @BindView(R.id.driver_name_tv)
    TextView driverNameTv;
    private CoreUserData user;
    private DashboardPresenter dashboardPresenter;
    private ShakeListener mShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.dashboard_title));


        checkPermission();

        dailyPics.setOnClickListener(this);
        tasleem.setOnClickListener(this);
        user = SharedPreferenceHelper.getInstance(this).getCoreUserData();
        PresenterComponent component = DaggerPresenterComponent.builder()
                .acc(this)
                .build();
        dashboardPresenter = component.getDashboardPresenter();
        dashboardPresenter.attachView(this);
        driverNameTv.setText(String.format(getString(R.string.welcome_name), user.getName()));


        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(100);
                startActivity(new Intent(DashboardActivity.this, NewIssueActivity.class));

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_drawer, menu);
        return true;
    }

    @Override
    protected void onPause() {
        mShaker.pause();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_issue:
                startActivity(new Intent(this, NewIssueActivity.class));
                return true;
            case R.id.logout:
                logoutAction(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        dashboardPresenter.getCars(user.getUserId());
        mShaker.resume();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.daily_pics:
                if (user.getCarInfo().size() == 1) {
                    intent = new Intent(this, CaptureActivity.class);
                    intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, user.getCarInfo().get(0).getId());
                } else {
                    intent = new Intent(this, SelectCarActivity.class);
                }
                intent.putExtra(Constant.INTENT_EXTRAS.isFrom, Constant.INTENT_EXTRAS.NewCase);
                startActivity(intent);
                break;
            case R.id.tasleem:
                if (user.getCarInfo() == null) {
                    showToast(getString(R.string.no_cars));
                    return;
                }
                if (user.getCarInfo().size() == 1) {
                    intent = new Intent(this, SelectDriverActivity.class);
                    intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, user.getCarInfo().get(0).getId());
                } else {
                    intent = new Intent(this, SelectCarActivity.class);
                }
                intent.putExtra(Constant.INTENT_EXTRAS.isFrom, Constant.INTENT_EXTRAS.SelectReceiver);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetCarsSuccess(List<LoginResponse.CarInfo> cars) {
        user.setCarInfo(cars);
    }

    @Override
    public void onGetCarsFail(String error) {

    }

    @Override
    public void setLoading(int apiCode) {

    }

    @Override
    public void setLoaded(int apiCode) {
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showOverlayDialog();
                return false; // above will start new Activity with proper app setting
            }
        }
        return true; // on lower OS versions granted during apk installation
    }

    private void showOverlayDialog() {
        if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
            final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", getPackageName());
            new AlertDialog.Builder(this)
                    .setTitle("Please Enable the additional permissions")
                    .setMessage("You will not receive notifications while the app is in background if you disable these permissions")
                    .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false)
                    .show();
        } else {

            AlertDialog.Builder alertadd = new AlertDialog.Builder(DashboardActivity.this);
            LayoutInflater factory = LayoutInflater.from(DashboardActivity.this);
            final View view = factory.inflate(R.layout.sample, null);
            ImageView gifImageView = (ImageView) view.findViewById(R.id.dialog_imageview);
            Glide.with(this).load(R.drawable.dialog).into(gifImageView);
            alertadd.setView(view)
                    .setCancelable(false)
                    .setTitle(getString(R.string.overlay_title))
                    .setMessage(getString(R.string.overlay_message))
                    .setNeutralButton(getString(R.string.enable), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                checkPermission();
            } else {
            }

        }

    }
}
