package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface SyncAddressMvpPresentder<M extends BtcModel, V extends SyncAddressMvpView> extends MvpPresenter<V> {

    void syncLastAddressIndex(long indexNo, long changeIndexNo, Wallet wallet);
}
