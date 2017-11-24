package com.bitbill.www.ui.wallet.importing;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
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
        if (!isValidWallet() || !isValidMnemonic()) {
            return;
        }
        // 校验助记词是否正确
        BitcoinJsWrapper.getInstance().validateMnemonicAndReturnSeedHex(getMvpView().getMnemonic(), wallet.getTradePwd(), new BitcoinJsWrapper.JsInterface.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if ("true".equals(jsResult[0])) {
                    //更新wallet对象
                    StringUtils.encryptMnemonicAndSeedHex(getMvpView().getMnemonic(), jsResult[1], wallet.getTradePwd(), wallet);
                    getCompositeDisposable().add(getModelManager()
                            .updateWallet(wallet)
                            .compose(applyScheduler())
                            .subscribeWith(new BaseSubcriber<Boolean>(getMvpView()) {
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    super.onNext(aBoolean);
                                    if (!isViewAttached()) {
                                        return;
                                    }
                                    if (aBoolean) {
                                        getMvpView().importWalletSuccess();
                                    } else {
                                        getMvpView().importWalletFail();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    if (!isViewAttached()) {
                                        return;
                                    }
                                }
                            })
                    );
                } else {
                    getMvpView().inputMnemonicError();
                }
            }
        });

    }

    public boolean isValidWallet() {
        if (getMvpView().getWallet() == null) {
            getMvpView().getWalletInfoFail();
            return false;
        }
        return true;
    }

    public boolean isValidMnemonic() {
        // TODO: 2017/11/23 判断助记词格式是否正确
        if (StringUtils.isEmpty(getMvpView().getMnemonic())) {
            getMvpView().getMnemonicFail();
            return false;
        }
        return true;
    }
}
