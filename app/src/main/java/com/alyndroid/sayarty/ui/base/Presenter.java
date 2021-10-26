package com.alyndroid.sayarty.ui.base;


public interface Presenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

}