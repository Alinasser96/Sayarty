package com.alyndroid.sayarty.ui.selectDriver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.sayarty.R;
import com.alyndroid.sayarty.data.local.SharedPreferenceHelper;
import com.alyndroid.sayarty.di.component.DaggerPresenterComponent;
import com.alyndroid.sayarty.di.component.PresenterComponent;
import com.alyndroid.sayarty.pojo.CoreUserData;
import com.alyndroid.sayarty.pojo.Driver;
import com.alyndroid.sayarty.ui.base.BaseActivity;
import com.alyndroid.sayarty.ui.daily.CaptureActivity;
import com.alyndroid.sayarty.util.CommonUtils;
import com.alyndroid.sayarty.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;

public class SelectDriverActivity extends BaseActivity implements SelectDriverView, View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.names_spinner)
    SearchableSpinner namesSpinner;
    private SelectDriverPresenter selectDriverPresenter;
    public List<Driver> drivers;
    private CoreUserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.select_receiver_title));
        PresenterComponent component = DaggerPresenterComponent.builder()
                .acc(this)
                .build();
        user = SharedPreferenceHelper.getInstance(this).getCoreUserData();
        confirm.setOnClickListener(this);
        selectDriverPresenter = component.getSelectDriverPresenter();
        selectDriverPresenter.attachView(this);
        selectDriverPresenter.getDrivers();
    }

    @Override
    public void onGetDriversSuccess(List<Driver> drivers) {
        this.drivers = drivers;
        List<String> names = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.getID() == user.getUserId())
                continue;
            names.add(driver.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        namesSpinner.setAdapter(adapter);
    }

    @Override
    public void onGetDriversFail(String error) {
        CommonUtils.showToast(this, getString(R.string.no_internet_message));
        finish();
    }

    @Override
    public void setLoading(int apiCode) {

    }

    @Override
    public void setLoaded(int apiCode) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra(Constant.INTENT_EXTRAS.isFrom, Constant.INTENT_EXTRAS.SelectReceiver);
                intent.putExtra(Constant.INTENT_EXTRAS.receiverID, getSelectedID() + "");
                intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, (String) getIntent().getExtras().get(Constant.INTENT_EXTRAS.CAR_ID));
                startActivity(intent);
                break;
        }
    }

    public int getSelectedID() {
        for (Driver driver : drivers) {
            if (driver.getName().equals(namesSpinner.getSelectedItem().toString())) {
                return driver.getID();
            }
        }
        return 0;
    }
}
