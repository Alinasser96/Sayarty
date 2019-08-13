package com.alyndroid.syarty.ui.selectCar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.alyndroid.syarty.ui.daily.CaptureActivity;
import com.alyndroid.syarty.ui.selectDriver.SelectDriverActivity;
import com.alyndroid.syarty.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCarActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.names_spinner)
    Spinner namesSpinner;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.select_car_hint)
    TextView selectCarHint;
    private CoreUserData user;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.select_car_title));
        confirm.setOnClickListener(this);
        user = SharedPreferenceHelper.getInstance(this).getCoreUserData();

        if (user.getCarInfo().size() == 0) {
            selectCarHint.setText(getString(R.string.no_cars));
            confirm.setText(getString(R.string.back));
            isEmpty = true;
            namesSpinner.setVisibility(View.GONE);
        } else {
            List<String> numbers = new ArrayList<>();
            for (LoginResponse.CarInfo driver : user.getCarInfo()) {
                numbers.add(driver.getCar_number());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, numbers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            namesSpinner.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (isEmpty) {
                    this.onBackPressed();
                } else {
                    Intent intent;
                    switch ((String) getIntent().getExtras().get(Constant.INTENT_EXTRAS.isFrom)) {
                        case Constant.INTENT_EXTRAS.NewCase:
                            selectCarHint.setText(getString(R.string.select_car2));
                            intent = new Intent(this, CaptureActivity.class);
                            intent.putExtra(Constant.INTENT_EXTRAS.isFrom, Constant.INTENT_EXTRAS.NewCase);
                            intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, getCarId());
                            startActivity(intent);
                            break;
                        case Constant.INTENT_EXTRAS.SelectReceiver:
                            intent = new Intent(this, SelectDriverActivity.class);
                            intent.putExtra(Constant.INTENT_EXTRAS.CAR_ID, getCarId());
                            startActivity(intent);
                            break;
                    }

                }
                break;
        }

    }

    private String getCarId() {
        for (LoginResponse.CarInfo car : user.getCarInfo()) {
            if (car.getCar_number().equals(namesSpinner.getSelectedItem())) {
                return car.getId();
            }
        }
        return "";
    }
}
