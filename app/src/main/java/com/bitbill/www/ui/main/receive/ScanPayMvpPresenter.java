package com.bitbill.www.ui.main.receive;

import com.bitbill.www.common.base.presenter.MvpPresenter;

/**
 * Created by isanwenyu on 2017/12/25.
 */

public interface ScanPayMvpPresenter<V extends ScanPayMvpView> extends MvpPresenter<V> {

    void createReceiveQrcode();
}
