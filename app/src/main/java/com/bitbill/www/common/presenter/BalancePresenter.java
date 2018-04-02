package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2018/2/5.
 */
@PerActivity
public class BalancePresenter<M extends WalletModel, V extends BalanceMvpView> extends ModelPresenter<M, V> implements BalanceMvpPresenter<M, V> {
    @Inject
    public BalancePresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getBalance() {
        if (!isValidWallets()) {
            return;
        }
        List<Wallet> wallets = getMvpView().getWallets();
        String extendedKeysHash = StringUtils.buildExtendedKeysHash(wallets);
        if (StringUtils.isEmpty(extendedKeysHash)) {
            getMvpView().getWalletsFail();
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .getBalance(new GetBalanceRequest(extendedKeysHash))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse>() {
                    @Override
                    public void onNext(ApiResponse stringApiResponse) {
                        if (handleApiResponse(stringApiResponse)) {
                            return;
                        }
                        if (stringApiResponse.isSuccess()) {
                            try {
                                JSONObject dataJsonObj = new JSONObject(String.valueOf(stringApiResponse.getData()));
                                long totalAmount = 0;
                                //设置钱包余额
                                for (Wallet wallet : wallets) {
                                    JSONObject amountJsonObj = dataJsonObj.optJSONObject(wallet.getName());
                                    if (amountJsonObj == null) {
                                        continue;
                                    }
                                    long balance = amountJsonObj.getLong("balance");
                                    wallet.setBalance(balance);
                                    wallet.setUnconfirm(amountJsonObj.getLong("unconfirm"));
                                    totalAmount += balance;
                                }
                                updateWallets(wallets, totalAmount);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loadBalance();
                            }
                        } else {
                            loadBalance();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        }
                        loadBalance();
                    }
                })
        );
    }

    private void updateWallets(List<Wallet> wallets, long totalAmount) {
        getCompositeDisposable().add(getModelManager().updateWallets(wallets)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (aBoolean) {
                            getMvpView().getBalanceSuccess(wallets, totalAmount);
                        } else {
                            loadBalance();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loadBalance();
                    }
                }));
    }

    @Override
    public void loadBalance() {
        if (!isValidWallets()) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .getAllWallets()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<Wallet>>() {
                    @Override
                    public void onNext(List<Wallet> walletList) {
                        super.onNext(walletList);
                        if (!isViewAttached()) {
                            return;
                        }//设置钱包余额
                        long totalAmount = 0;
                        for (Wallet wallet : walletList) {
                            totalAmount += wallet.getBalance();
                        }
                        getMvpView().getBalanceSuccess(walletList, totalAmount);


                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().getBalanceFail();
                        }

                    }
                })
        );
    }

    public boolean isValidWallets() {
        if (StringUtils.isEmpty(getMvpView().getWallets())) {
            getMvpView().getWalletsFail();
            return false;
        }
        return true;
    }

}
