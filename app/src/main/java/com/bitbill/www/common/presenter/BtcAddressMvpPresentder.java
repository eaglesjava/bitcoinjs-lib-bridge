package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface BtcAddressMvpPresentder<M extends WalletModel, V extends BtcAddressMvpView> extends MvpPresenter<V> {
    /**
     *
     */
    void newAddress();
}
