package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;

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
    void createMnemonic();


    /**
     * 插入钱包
     */
    void insertWallet();

    /**
     * 创建钱包
     */
    void createWallet();

    /**
     * 导入钱包
     */
    void importWallet();

    void resetPwd();
}
