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
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.ListTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */

public class MainPresenter<M extends TxModel, V extends MainMvpView> extends ModelPresenter<M, V>
        implements MainMvpPresenter<M, V> {

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(M appModel, SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(appModel, schedulerProvider, compositeDisposable);
    }

    @Override
    public void listUnconfirm() {

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
        getCompositeDisposable().add(getModelManager().getUnConfirmedTxRecord()
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

