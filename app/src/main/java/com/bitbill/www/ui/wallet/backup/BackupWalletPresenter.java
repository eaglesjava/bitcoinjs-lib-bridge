package com.bitbill.www.ui.wallet.backup;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

/**
 * Created by isanwenyu@163.com on 2017/11/23.
 */
public class BackupWalletPresenter<W extends WalletModel, V extends BackupWalletMvpView> extends ModelPresenter<W, V> implements BackupWalletMvpPresenter<W, V> {

    @Inject
    public BackupWalletPresenter(W model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadMnemonic(String confirmPwd) {
        if (!isValidWallet() || !isValidConfirmPwd(confirmPwd)) {
            return;
        }
        getCompositeDisposable().add(Observable.just(getMvpView().getWallet())
                .compose(this.applyScheduler())
                .concatMap(new Function<Wallet, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Wallet wallet) throws Exception {
                        //解密助记词
                        String encryptMnemonic = wallet.getEncryptMnemonic();
                        String decryptMnemonic = StringUtils.decryptMnemonic(encryptMnemonic, confirmPwd);
                        //对比助记词hash是否一致
                        if (StringUtils.equals(StringUtils.getMnemonicHash(decryptMnemonic), wallet.getEncryptMnemonicHash())) {
                            return Observable.just(decryptMnemonic);
                        } else {
                            throw new Exception("the Mnemonic hash is inconsistent.");
                        }
                    }
                })
                .subscribeWith(new BaseSubcriber<String>(getMvpView()) {
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        if (!isValidMvpView()) {
                            return;
                        }
                        getMvpView().loadMnemonicSuccess(s);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isValidMvpView()) {
                            return;
                        }
                        getMvpView().loadMnemonicFail();
                    }
                }));

    }

    private boolean isValidConfirmPwd(String confirmPwd) {
        if (!StringUtils.isPasswordValid(confirmPwd)) {
            getMvpView().getConfirmPwdFail();
            return false;
        }
        return true;
    }

    private boolean isValidWallet() {
        if (getMvpView().getWallet() == null) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

}
