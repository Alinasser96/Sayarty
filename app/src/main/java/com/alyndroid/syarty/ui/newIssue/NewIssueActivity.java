package com.alyndroid.syarty.ui.newIssue;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class NewIssueActivity extends BaseActivity implements NewIssueView, View.OnClickListener {

    @BindView(R.id.sp_issues)
    Spinner spIssues;
    @BindView(R.id.et_new_issue)
    EditText etNewIssue;
    @BindView(R.id.issue_content_text_input_layout)
    TextInputLayout issueContentTextInputLayout;
    @BindView(R.id.btn_confirm_issue)
    Button btnConfirmIssue;
    @BindView(R.id.cars_title)
    TextView carsTitle;
    @BindView(R.id.sp_cars)
    Spinner spCars;
    private CoreUserData user;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NewIssuePresenter newIssuePresenter;
    private boolean isNoCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_issue);
        ButterKnife.bind(this);
        newIssuePresenter = new NewIssuePresenter(compositeDisposable, this);
        newIssuePresenter.attachView(this);
        btnConfirmIssue.setOnClickListener(this);
        user = SharedPreferenceHelper.getInstance(this).getCoreUserData();
        if (user.getCarInfo().size() == 0) {
            isNoCar = true;
        } else {
            List<String> numbers = new ArrayList<>();
            for (LoginResponse.CarInfo driver : user.getCarInfo()) {
                numbers.add(driver.getCar_number());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, numbers);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCars.setAdapter(adapter);

            if (numbers.size() > 1) {
                showViews(spCars, carsTitle);
            } else if (numbers.size() == 1) {
                spCars.setSelection(0);
            }
        }

    }

    @Override
    public void onSendIssueSuccess() {
        showToast(getString(R.string.issueConfirmed));
        finish();
    }

    @Override
    public void onSendIssueFail() {
        showToast(getString(R.string.some_error));
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

            case R.id.btn_confirm_issue:
                if (etNewIssue.getText().toString().equals("")) {
                    issueContentTextInputLayout.setError(getString(R.string.must_enter_content));
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("user_id", SharedPreferenceHelper.getInstance(this).getCoreUserData().getUserId());
                    if (!isNoCar) {
                        map.put("car_id", getCarId());
                    }
                    map.put("content", etNewIssue.getText().toString());
                    map.put("belong_to", spIssues.getSelectedItem());
                    newIssuePresenter.sendIssue(map);
                }
                break;
        }
    }


    private String getCarId() {
        for (LoginResponse.CarInfo car : user.getCarInfo()) {
            if (car.getCar_number().equals(spCars.getSelectedItem())) {
                return car.getId();
            }
        }
        return "";
    }
}
