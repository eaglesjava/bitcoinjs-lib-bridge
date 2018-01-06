package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface BtcAddressMvpPresentder<M extends WalletModel, V extends BtcAddressMvpView> extends MvpPresenter<V> {
    /**
     *
     */
    void newAddress();

    void getBitcoinAddressByMasterXPublicKey(long index, Wallet wallet);

    void requestRefreshAddress(long index);

    void checkLastAddressIndex(long indexNo, long lastIndex, Wallet wallet);

    void getBitcoinContinuousAddress(long fromIndex, long toIndex, Wallet wallet);

    void updateAddressIndex(Wallet wallet, String[] addressArray);
}
