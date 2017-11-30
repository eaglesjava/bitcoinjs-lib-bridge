package com.bitbill.www.ui.wallet.init;

import android.util.Log;

import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
@PerActivity
public class InitWalletSuccessPresenter<M extends WalletModel, V extends InitWalletSuccessMvpView> extends ModelPresenter<M, V> implements InitWalletSuccessMvpPresenter<M, V> {
    private static final String TAG = "InitWalletSuccess";
    private Wallet mWallet;

    @Inject
    public InitWalletSuccessPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void createWallet() {
        mWallet = getMvpView().getWallet();
        BitcoinJsWrapper.getInstance().getBitcoinMasterXPublicKey(mWallet.getSeedHex(), new BitcoinJsWrapper.JsInterface.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                getCompositeDisposable().add(getModelManager().createWallet(mWallet.getName(), jsResult[0], DeviceUtil.getDeviceId(), getDeviceToken(), mWallet.getLastAddressIndex())
                        .compose(applyScheduler())
                        .subscribeWith(new BaseSubcriber<ApiResponse<String>>() {
                            @Override
                            public void onNext(ApiResponse<String> stringApiResponse) {
                                super.onNext(stringApiResponse);
                                if (!isValidMvpView()) {
                                    return;
                                }
                                Log.d(TAG, "onNext() called with: stringApiResponse = [" + stringApiResponse + "]");
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                if (!isValidMvpView()) {
                                    return;
                                }
                                Log.e(TAG, "onError: ", e);

                            }
                        }));
            }
        });

    }


    public String getDeviceToken() {
        return "";
    }

}
