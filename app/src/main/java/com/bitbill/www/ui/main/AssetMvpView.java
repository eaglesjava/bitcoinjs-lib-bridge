package com.bitbill.www.ui.main;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/11/28.
 */
public interface AssetMvpView extends MvpView {

    void loadWalletsSuccess(List<Wallet> wallets);

    void loadWalletsFail();
}
