package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;

/**
 * Created by isanwenyu on 2018/1/30.
 */

public interface TransferDetailMvpPresenter<M extends BtcModel, V extends TransferDetailMvpView> extends MvpPresenter<V> {

    void buidTransferData();
}
