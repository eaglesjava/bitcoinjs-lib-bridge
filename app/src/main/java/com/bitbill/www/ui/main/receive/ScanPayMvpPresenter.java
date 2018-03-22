package com.bitbill.www.ui.main.receive;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;

/**
 * Created by isanwenyu on 2017/12/25.
 */

public interface ScanPayMvpPresenter<M extends BtcModel, V extends ScanPayMvpView> extends MvpPresenter<V> {

    void createReceiveQrcode();

    void getTxInfo();
}
