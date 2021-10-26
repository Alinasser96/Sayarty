package com.alyndroid.sayarty.ui.daily;

import com.alyndroid.sayarty.pojo.StoreResponse;
import com.alyndroid.sayarty.ui.base.BaseView;

public interface DailyView extends BaseView {
    void onStoreSuccess(StoreResponse storeResponse);
    void onStoreFail(String message);

    void onSendToReceiverSuccess();
    void onSendToReceiverFail(String error);
}
