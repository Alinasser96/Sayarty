package com.alyndroid.sayarty.ui.selectDriver;

import com.alyndroid.sayarty.pojo.Driver;
import com.alyndroid.sayarty.ui.base.BaseView;

import java.util.List;

public interface SelectDriverView extends BaseView {
    void onGetDriversSuccess(List<Driver> drivers);
    void onGetDriversFail(String error);
}
