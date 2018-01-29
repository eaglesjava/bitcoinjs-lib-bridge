package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2017/12/1.
 */
public interface SplashMvpView extends MvpView {
    void hasWallet(List<Wallet> walletList);
}
