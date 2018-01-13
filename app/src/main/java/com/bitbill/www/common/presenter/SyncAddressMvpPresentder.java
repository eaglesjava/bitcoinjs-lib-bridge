package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface SyncAddressMvpPresentder<M extends AddressModel, V extends SyncAddressMvpView> extends MvpPresenter<V> {

    void syncLastAddressIndex(long indexNo, Wallet wallet);
}
