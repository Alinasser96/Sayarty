package com.alyndroid.syarty.ui.daily;

import com.alyndroid.syarty.pojo.StoreResponse;
import com.alyndroid.syarty.ui.base.BaseView;

public interface DailyView extends BaseView {
    void onStoreSuccess(StoreResponse storeResponse);
    void onStoreFail(String message);

    void onSendToReceiverSuccess();
    void onSendToReceiverFail(String error);
}
