package com.bitbill.www.ui.wallet.importing;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */

public class ImportWalletPresenter<M extends WalletModel, V extends ImportWalletMvpView> extends ModelPresenter<M, V> implements ImportWalletMvpPresenter<M, V> {
    @Inject
    public ImportWalletPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    /**
     * import a wallet by the mnemonic
     *
     * @param wallet
     */
    @Override
    public void importWallet(Wallet wallet) {
        // TODO: 2017/11/23 校验助记词是否正确


    }
}
