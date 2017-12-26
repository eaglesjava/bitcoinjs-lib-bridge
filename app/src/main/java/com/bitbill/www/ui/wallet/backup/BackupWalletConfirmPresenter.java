package com.bitbill.www.ui.wallet.backup;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public class BackupWalletConfirmPresenter<M extends WalletModel, V extends BackupWalletConfirmMvpView> extends ModelPresenter<M, V> implements BackupWalletConfirmMvpPresenter<M, V> {

    @Inject
    public BackupWalletConfirmPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void checkBackup() {
        if (!isMnemonicCorrect()) {
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        wallet.setIsBackuped(true);
        String mnemonic = wallet.getMnemonic();
        wallet.setMnemonic("");
        getCompositeDisposable().add(getModelManager().updateWallet(wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>(getMvpView()) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {

                            getMvpView().backupSuccess();
                        } else {
                            getMvpView().backupFail();
                            //reset backup status
                            wallet.setIsBackuped(false);
                            wallet.setMnemonic(mnemonic);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().backupFail();
                        //reset backup status
                        wallet.setIsBackuped(false);
                        wallet.setMnemonic(mnemonic);
                    }
                }));


    }

    /**
     * 校验助记词是否匹配
     *
     * @return
     */
    private boolean isMnemonicCorrect() {
        if (StringUtils.isEmpty(getMvpView().getMnemonicConfirmList())) {
            getMvpView().backupFail();
            return false;
        }
        if (StringUtils.isEmpty(getMvpView().getMnemonicArray())) {
            getMvpView().backupFail();
            return false;
        }
        if (!Arrays.equals(getMvpView().getMnemonicConfirmList().toArray(), getMvpView().getMnemonicArray())) {

            getMvpView().backupFail();
            return false;
        }
        return true;
    }
}
