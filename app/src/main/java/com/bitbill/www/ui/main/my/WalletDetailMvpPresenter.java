package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu on 2017/12/28.
 */

public interface WalletDetailMvpPresenter<M extends WalletModel, V extends MvpView> extends MvpPresenter<V> {

    void createWalletIdQrcode();

    void deleteWallet();
}
