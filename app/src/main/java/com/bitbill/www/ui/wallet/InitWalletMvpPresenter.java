package com.bitbill.www.ui.wallet;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
@PerActivity
public interface InitWalletMvpPresenter<W extends WalletModel, V extends InitWalletMvpView> extends MvpPresenter<V> {
    /**
     * 导入钱包
     */
    void initWallet();

    /**
     * 获取助记词
     */
    void createMnemonic(Wallet wallet);
}
