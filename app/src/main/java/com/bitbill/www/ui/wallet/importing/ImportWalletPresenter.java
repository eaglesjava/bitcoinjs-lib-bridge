package com.bitbill.www.ui.wallet.importing;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;

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

    @Override
    public void checkMnemonic() {
        if (!isValidMnemonic()) {
            return;
        }
        getMvpView().showLoading();
        String mnemonicHash = StringUtils.getSHA256Hex(getMvpView().getMnemonic());
        getCompositeDisposable().add(getModelManager().getWalletByMnemonicHash(mnemonicHash)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Wallet>() {
                    @Override
                    public void onNext(Wallet wallet) {
                        super.onNext(wallet);
                        if (wallet != null) {
                            //助记词已存在
                            getMvpView().hasExsistMnemonic(wallet);
                            getMvpView().hideLoading();
                        } else {
                            importWallet();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof NullPointerException) {
                            // Callable returned null
                            importWallet();
                        } else {
                            getMvpView().importWalletFail();
                            getMvpView().hideLoading();
                        }

                    }
                }));
    }

    /**
     * import a wallet by the mnemonic
     */
    @Override
    public void importWallet() {
        if (!isValidMnemonic()) {
            getMvpView().hideLoading();
            return;
        }
        // 校验助记词是否正确
        try {
            BitcoinJsWrapper.getInstance().validateMnemonicReturnSeedHexAndXPublicKey(getMvpView().getMnemonic(), new BitcoinJsWrapper.Callback() {
                @Override
                public void call(String key, String... jsResult) {
                    if (jsResult != null && "true".equals(jsResult[0]) && jsResult.length > 2) {
                        //更新wallet对象
                        String seedHex = jsResult[1];
                        String XPublicKey = jsResult[2];
                        String extendedKeysHash = EncryptUtils.encryptMD5ToString(XPublicKey);
                        String mnemonicHash = EncryptUtils.encryptMD5ToString(getMvpView().getMnemonic());
                        Wallet wallet = new Wallet();
                        wallet.setXPublicKey(XPublicKey);
                        wallet.setMnemonicHash(mnemonicHash);
                        //不需要备份
                        wallet.setIsBackuped(true);
                        wallet.setSeedHex(seedHex);
                        getCompositeDisposable().add(getModelManager()
                                .getWalletId(new GetWalletIdRequest(extendedKeysHash))
                                .compose(applyScheduler())
                                .subscribeWith(new BaseSubcriber<ApiResponse<GetWalletIdResponse>>() {
                                    @Override
                                    public void onNext(ApiResponse<GetWalletIdResponse> apiResponse) {
                                        super.onNext(apiResponse);
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        if (apiResponse != null) {
                                            if (apiResponse.getStatus() == ApiResponse.STATUS_CODE_SUCCESS) {
                                                if (apiResponse.getData() != null && StringUtils.isNotEmpty(apiResponse.getData().getWalletId())) {
                                                    wallet.setName(apiResponse.getData().getWalletId());
                                                }
                                                getMvpView().importWalletSuccess(wallet);
                                            } else {
                                                getMvpView().importWalletFail();
                                            }

                                        }
                                        getMvpView().hideLoading();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        getMvpView().hideLoading();

                                        // handle error here
                                        if (e instanceof ANError) {
                                            ANError anError = (ANError) e;
                                            handleApiError(anError);
                                        }
                                    }
                                })
                        );
                    } else {
                        getMvpView().inputMnemonicError();
                        getMvpView().hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().inputMnemonicError();
            getMvpView().hideLoading();
        }

    }

    public boolean isValidMnemonic() {
        // 判断助记词格式是否正确
        if (!StringUtils.isValidMnemonic(getMvpView().getMnemonic())) {
            getMvpView().getMnemonicFail();
            return false;
        }
        return true;
    }
}
