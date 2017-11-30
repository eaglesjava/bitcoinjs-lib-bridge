package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public interface InitWalletSuccessMvpView extends MvpView {
    Wallet getWallet();
}
