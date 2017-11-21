package com.bitbill.www.ui.wallet;

import android.text.TextUtils;
import android.util.Log;

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
public class InitWalletPresenter<W extends WalletModel, V extends InitWalletMvpView> extends ModelPresenter<W, V> implements InitWalletMvpPresenter<W, V> {

    private static final String TAG = "InitWalletPresenter";

    @Inject
    public InitWalletPresenter(W model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    /**
     * 导入钱包
     */
    @Override
    public void initWallet() {
        //  有效性校验 && 合法性校验
        if (!isValidWalletName() || !isValidTradePwd() || !isValidConfirmTradePwd()) {
            return;
        }

        // 初始化钱包实体
        Wallet wallet = new Wallet();
        wallet.setCreatedAt(System.currentTimeMillis());
        wallet.setUpdatedAt(System.currentTimeMillis());
        wallet.setName(getMvpView().getWalletName());

        getCompositeDisposable().add(getModelManager()
                .insertWallet(wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Long>(getMvpView()) {
                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
                        Log.d(TAG, "initWalletSuccess walletid = [" + aLong + "]");
                        if (!isValidMvpView()) {
                            return;
                        }
                        getMvpView().initWalletSuccess(wallet);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "initWalletFail ", e);
                        if (!isValidMvpView()) {
                            return;
                        }
                        getMvpView().initWalletFail();

                    }
                }));

    }

    private boolean isValidWalletName() {
        // Check for a valid wallet name.
        if (TextUtils.isEmpty(getMvpView().getWalletName())) {
            getMvpView().requireWalletName();
            return false;
        }
        if (!StringUtils.isWalletNameValid(getMvpView().getWalletName())) {
            getMvpView().invalidWalletName();
            return false;
        }
        return true;
    }

    private boolean isValidTradePwd() {
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(getMvpView().getTradePwd())) {
            getMvpView().requireTradePwd();
            return false;
        }
        if (!StringUtils.isPasswordValid(getMvpView().getTradePwd())) {
            getMvpView().invalidTradePwd();
            return false;
        }
        return true;
    }

    private boolean isValidConfirmTradePwd() {
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(getMvpView().getConfirmTradePwd())) {
            getMvpView().requireTradeConfirmPwd();
            return false;
        }
        if (!StringUtils.isPasswordValid(getMvpView().getConfirmTradePwd())) {
            getMvpView().isPwdInConsistent();
            return false;
        }
        if (!isPwdConsistent()) {
            getMvpView().isPwdInConsistent();
            return false;
        }
        return true;
    }


    private boolean isPwdConsistent() {
        return TextUtils.equals(getMvpView().getTradePwd(), getMvpView().getConfirmTradePwd());
    }

    /**
     * 获取助记词
     */
    @Override
    public void createMnemonic(Wallet wallet) {
        if (!isValidTradePwd()) {
            return;
        }
        if (wallet == null) {
            getMvpView().initWalletFail();
            return;
        }
        //  create mnemonic in js thread
        getMvpView().showLoading();
        BitcoinJsWrapper.getInstance().generateMnemonicRandomCN(new BitcoinJsWrapper.JsInterface.Callback() {
            @Override
            public void call(String key, String jsResult) {
                Log.d(TAG, "generateMnemonicRandomCN: key = [" + key + "], Mnemonic = [" + jsResult + "]");
                String encryptMnemonicHash = StringUtils.encryptMnemonic(jsResult, InitWalletPresenter.this.getMvpView().getTradePwd(), wallet);
                Log.d(TAG, "update walelt: " + wallet.toString());
                getCompositeDisposable().add(getModelManager()
                        .updateWallet(wallet)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribeWith(new BaseSubcriber<Boolean>() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                super.onNext(aBoolean);
                                Log.d(TAG, "createMnemonicSuccess = [" + encryptMnemonicHash + "]");
                                if (!isViewAttached()) {
                                    return;
                                }
                                if (aBoolean) {
                                    getMvpView().createMnemonicSuccess(encryptMnemonicHash);
                                } else {
                                    getMvpView().createMnemonicFail();
                                }
                                getMvpView().hideLoading();
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                Log.e(TAG, "createMnemonicFail ", e);
                                if (!isViewAttached()) {
                                    return;
                                }
                                getMvpView().createMnemonicFail();
                                getMvpView().hideLoading();
                            }
                        })
                );
            }
        });

    }

}
