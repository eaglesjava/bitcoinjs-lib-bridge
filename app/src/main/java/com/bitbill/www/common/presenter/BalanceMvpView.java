package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu on 2018/2/5.
 */

public interface BalanceMvpView extends MvpView {

    List<Wallet> getWallets();

    void getWalletsFail();

    void getBalanceFail();

    void getBalanceSuccess(List<Wallet> wallets, Long allAmount);
}
