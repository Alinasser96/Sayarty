package com.alyndroid.syarty.ui.selectDriver;

import com.alyndroid.syarty.pojo.Driver;
import com.alyndroid.syarty.ui.base.BaseView;

import java.util.List;

public interface SelectDriverView extends BaseView {
    void onGetDriversSuccess(List<Driver> drivers);
    void onGetDriversFail(String error);
}
