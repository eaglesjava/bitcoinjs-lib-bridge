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
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public class MainPresenter<M extends WalletModel, V extends MainMvpView> extends ModelPresenter<M, V>
        implements MainMvpPresenter<M, V> {

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(M appModel, SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(appModel, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadWallet() {
        getCompositeDisposable().add(getModelManager().getAllWallets()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<Wallet>>(getMvpView()) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(List<Wallet> wallets) {
                        super.onNext(wallets);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (!StringUtils.isEmpty(wallets)) {

                            getMvpView().loadWalletsSuccess(wallets);
                        } else {

                            getMvpView().loadWalletsFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError((ANError) e);
                        }
                    }
                }));
    }

    @Override
    public void getBalance() {
        if (!isValidWallets()) {
            return;
        }
        List<Wallet> wallets = getMvpView().getWallets();
        String extendedKeysHash = "";
        for (int i = 0; i < wallets.size(); i++) {
            // TODO: 2017/12/21 check xpubkey
            extendedKeysHash += EncryptUtils.encryptMD5ToString(wallets.get(i).getXPublicKey());
            if (i < wallets.size() - 1) {
                extendedKeysHash += "|";
            }
        }
        getCompositeDisposable().add(getModelManager()
                .getBalance(new GetBalanceRequest(extendedKeysHash))
                .concatMap(stringApiResponse -> {
                    boolean check = false;
                    if (stringApiResponse.isSuccess()) {
                        JSONObject dataJsonObj = new JSONObject(String.valueOf(stringApiResponse.getData()));
                        //设置钱包余额
                        for (Wallet wallet : wallets) {
                            JSONObject amountJsonObj = dataJsonObj.getJSONObject(wallet.getName());
                            wallet.setBtcBalance(amountJsonObj.getLong("balance"));
                            wallet.setBtcUnconfirm(amountJsonObj.getLong("unconfirm"));
                            //更新钱包
                            getModelManager().updateWallet(wallet);
                        }
                        check = true;
                    }
                    return Observable.just(check);
                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {
                            getMvpView().getBalanceSuccess(wallets);
                        } else {
                            getMvpView().getBalanceFail();
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

