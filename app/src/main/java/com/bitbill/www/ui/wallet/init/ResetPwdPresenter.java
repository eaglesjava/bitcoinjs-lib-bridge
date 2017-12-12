package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
@PerActivity
public class ResetPwdPresenter<M extends WalletModel, V extends ResetPwdMvpView> extends ModelPresenter<M, V> implements ResetPwdMvpPresenter<M, V> {
    @Inject
    public ResetPwdPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void checkOldPwd() {
        getMvpView().getOldPwd();
        if (StringUtils.checkUserPwd(getMvpView().getOldPwd(), getMvpView().getWallet())) {
            resetPwd();
        } else {
            getMvpView().oldPwdError();
        }
    }

    @Override
    public void resetPwd() {
        Wallet wallet = getMvpView().getWallet();
        String mnemonic = StringUtils.decryptByPwd(wallet.getEncryptMnemonic(), getMvpView().getOldPwd());
        String seedHex = StringUtils.decryptByPwd(wallet.getEncryptSeed(), getMvpView().getOldPwd());
        StringUtils.encryptMnemonicAndSeedHex(mnemonic, seedHex, wallet.getXPublicKey(), getMvpView().getNewPwd(), wallet);
        //更新数据库
        getCompositeDisposable().add(getModelManager()
                .updateWallet(wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {
                            getMvpView().resetPwdSuccess();
                        } else {
                            getMvpView().resetPwdFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().resetPwdFail();
                    }
                }));
    }
}
