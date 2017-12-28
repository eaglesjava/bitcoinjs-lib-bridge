package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu on 2017/12/28.
 */

public interface WalletMvpView extends MvpView {
    void loadWalletsSuccess(List<Wallet> wallets);

    void loadWalletsFail();
}
