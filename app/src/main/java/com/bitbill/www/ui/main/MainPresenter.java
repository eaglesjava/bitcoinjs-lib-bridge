/*
 * Copyright (c) 2017 askcoin.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bitbill.www.ui.main;


import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public class MainPresenter<M extends WalletModel, V extends MainMvpView> extends ModelPresenter<M, V>
        implements MainMvpPresenter<M, V> {

    private static final String TAG = "MainPresenter";
    @Inject
    TxModel mTxModel;

    @Inject
    public MainPresenter(M appModel, SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(appModel, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getBalance() {
        if (!isValidWallets()) {
            return;
        }
        List<Wallet> wallets = getMvpView().getWallets();
        String extendedKeysHash = "";
        for (int i = 0; i < wallets.size(); i++) {
            // check xpubkey
            String xPublicKey = wallets.get(i).getExtentedPublicKey();
            extendedKeysHash += StringUtils.isEmpty(xPublicKey) ? "" : EncryptUtils.encryptMD5ToString(xPublicKey);
            if (i < wallets.size() - 1) {
                extendedKeysHash += "|";
            }
        }
        getCompositeDisposable().add(getModelManager()
                .getBalance(new GetBalanceRequest(extendedKeysHash))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse>() {
                    @Override
                    public void onNext(ApiResponse stringApiResponse) {
                        if (stringApiResponse != null && stringApiResponse.isSuccess()) {
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
                                updateWallets(wallets);
                                getMvpView().getBalanceSuccess(wallets, totalAmount);
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

    private void updateWallets(List<Wallet> wallets) {
        getCompositeDisposable().add(getModelManager().updateWallets(wallets)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
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

    @Override
    public void listUnconfirm() {

        if (!isValidWallets()) {
            return;
        }
        List<Wallet> wallets = getMvpView().getWallets();
        String extendedKeysHash = "";
        for (int i = 0; i < wallets.size(); i++) {
            // TODO: 2017/12/21 check xpubkey
            extendedKeysHash += EncryptUtils.encryptMD5ToString(wallets.get(i).getExtentedPublicKey());
            if (i < wallets.size() - 1) {
                extendedKeysHash += "|";
            }
        }
        getCompositeDisposable().add(mTxModel
                .listUnconfirm(new ListUnconfirmRequest(extendedKeysHash))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<ListTxElementResponse>>() {
                    @Override
                    public void onNext(ApiResponse<ListTxElementResponse> listApiResponse) {
                        super.onNext(listApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listApiResponse != null && listApiResponse.isSuccess()) {

                            ListTxElementResponse data = listApiResponse.getData();
                            if (data != null) {
                                getMvpView().listUnconfirmSuccess(StringUtils.removeDuplicateList(data.getList()));
                            } else {
                                getMvpView().listUnconfirmFail();
                            }

                        } else {
                            getMvpView().listUnconfirmFail();
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
                        } else {
                            getMvpView().listUnconfirmFail();
                        }

                    }
                })
        );

    }

    @Override
    public void loadUnConfirmedList() {
        getCompositeDisposable().add(mTxModel.getUnConfirmedTxRecord()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<TxRecord>>() {
                    @Override
                    public void onNext(List<TxRecord> txRecords) {
                        super.onNext(txRecords);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().loadUnconfirmSuccess(txRecords);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().loadUnconfirmFail();
                    }
                }));
    }

    public boolean isValidWallets() {
        if (StringUtils.isEmpty(getMvpView().getWallets())) {
            getMvpView().getWalletsFail();
            return false;
        }
        return true;
    }
}

