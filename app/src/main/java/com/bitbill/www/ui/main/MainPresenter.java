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


import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

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
                        if (!isValidMvpView()) {
                            return;
                        }
                        getMvpView().loadWalletsSuccess(wallets);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isValidMvpView()) {
                            return;
                        }
                        getMvpView().loadWalletsFail();
                    }
                }));
    }
}
