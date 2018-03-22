package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.btc.BtcModel;
import com.bitbill.www.model.btc.db.entity.Address;

import java.util.List;

/**
 * Created by isanwenyu on 2018/1/13.
 */

public interface WalletAddressMvpPresenter<M extends BtcModel, V extends WalletAddressMvpView> extends MvpPresenter<V> {
    void requestListUnspent();

    void updateAddressBalance(List<Address> addressList);
}
