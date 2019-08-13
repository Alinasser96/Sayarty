package com.alyndroid.syarty.ui.selectDriver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.di.component.DaggerPresenterComponent;
import com.alyndroid.syarty.di.component.PresenterComponent;
import com.alyndroid.syarty.pojo.Driver;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.ui.daily.CaptureActivity;
import com.alyndroid.syarty.util.CommonUtils;
import com.alyndroid.syarty.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDriverActivity extends BaseActivity implements SelectDriverView, View.OnClickListener {

    @BindView(R.id.names_spinner)
    Spinner namesSpinner;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.confirm)
    Button confirm;
    private SelectDriverPresenter selectDriverPresenter;
    public List<Driver> drivers;

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
            names.add(driver.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        namesSpinner.setAdapter(adapter);
    }

    @Override
    public void onGetDriversFail(String error) {
        CommonUtils.showToast(this, getString(R.string.some_error));
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
                intent.putExtra(Constant.INTENT_EXTRAS.isFrom,Constant.INTENT_EXTRAS.SelectReceiver);
                intent.putExtra(Constant.INTENT_EXTRAS.receiverID, getSelectedID()+"");
                intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, (String) getIntent().getExtras().get(Constant.INTENT_EXTRAS.CAR_ID));
                startActivity(intent);
                break;
        }
    }

    public int getSelectedID() {
        for(Driver driver:drivers){
            if (driver.getName().equals(namesSpinner.getSelectedItem().toString())){
                return driver.getID();
            }
        }
        return 0;
    }
}
