package com.alyndroid.syarty.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.di.component.DaggerPresenterComponent;
import com.alyndroid.syarty.di.component.PresenterComponent;
import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.ui.daily.CaptureActivity;
import com.alyndroid.syarty.ui.newIssue.NewIssueActivity;
import com.alyndroid.syarty.ui.selectCar.SelectCarActivity;
import com.alyndroid.syarty.ui.selectDriver.SelectDriverActivity;
import com.alyndroid.syarty.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseActivity implements View.OnClickListener, DashboardView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.daily_pics)
    LinearLayout dailyPics;
    @BindView(R.id.tasleem)
    LinearLayout tasleem;
    private CoreUserData user;
    private DashboardPresenter dashboardPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.dashboard_title));

        dailyPics.setOnClickListener(this);
        tasleem.setOnClickListener(this);
        user = SharedPreferenceHelper.getInstance(this).getCoreUserData();
        PresenterComponent component = DaggerPresenterComponent.builder()
                .acc(this)
                .build();
        dashboardPresenter = component.getDashboardPresenter();
        dashboardPresenter.attachView(this);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_issue:
                // code for option1
                startActivity(new Intent(this, NewIssueActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        dashboardPresenter.getCars(user.getUserId());
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
}
