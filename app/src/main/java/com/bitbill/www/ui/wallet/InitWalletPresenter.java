package com.bitbill.www.ui.wallet;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.model.wallet.WalletModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
public class InitWalletPresenter<W extends WalletModel, V extends InitWalletMvpView> extends ModelPresenter<W, V> implements InitWalletMvpPresenter<W, V> {

    @Inject
    public InitWalletPresenter(W model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    /**
     * 导入钱包
     */
    @Override
    public void initWallet() {
        // TODO: 2017/11/17 test
        getMvpView().initWalletSuccess();
    }

    /**
     * 获取助记词
     */
    @Override
    public void createMnemonic() {

    }
}
