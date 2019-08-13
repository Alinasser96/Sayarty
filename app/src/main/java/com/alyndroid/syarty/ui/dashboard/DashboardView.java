package com.alyndroid.syarty.ui.dashboard;

import com.alyndroid.syarty.pojo.LoginResponse;
import com.alyndroid.syarty.ui.base.BaseView;

import java.util.List;

public interface DashboardView extends BaseView {
    void onGetCarsSuccess(List<LoginResponse.CarInfo> cars);
    void onGetCarsFail(String error);
}
