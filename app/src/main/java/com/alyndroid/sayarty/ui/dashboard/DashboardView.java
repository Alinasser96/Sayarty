package com.alyndroid.sayarty.ui.dashboard;

import com.alyndroid.sayarty.pojo.LoginResponse;
import com.alyndroid.sayarty.ui.base.BaseView;

import java.util.List;

public interface DashboardView extends BaseView {
    void onGetCarsSuccess(List<LoginResponse.CarInfo> cars);
    void onGetCarsFail(String error);
}
