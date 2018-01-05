package com.bitbill.www.ui.main.receive;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
public interface BtcReceiveMvpPresenter<M extends WalletModel, V extends BtcReceiveMvpView> extends MvpPresenter<V> {

    void loadAddress(Wallet selectedWallet);

    void createAddressQrcode(String address);
}
