package com.bitbill.www.ui.wallet.init;

import android.text.TextUtils;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
@PerActivity
public class CreateWalletIdPresenter<M extends WalletModel, V extends CreateWalletIdMvpView> extends ModelPresenter<M, V> implements CreateWalletIdMvpPresenter<M, V> {
    @Inject
    public CreateWalletIdPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void checkWalletId() {

        if (!isValidWalletId()) {
            return;
        }
        getCompositeDisposable()
                .add(getModelManager()
                        .checkWalletId(new CheckWalletIdRequest(getMvpView().getWalletId()))
                        .compose(this.applyScheduler())
                        .subscribeWith(new BaseSubcriber<ApiResponse>(getMvpView()) {
                            @Override
                            public void onNext(ApiResponse stringApiResponse) {
                                super.onNext(stringApiResponse);
                                if (!isViewAttached()) {
                                    return;
                                }
                                if (stringApiResponse != null) {
                                    if (stringApiResponse.isSuccess()) {
                                        getMvpView().checkWalletIdSuccess();
                                    } else if (stringApiResponse.isWalletIdExsist()) {
                                        getMvpView().hasWalletIdExsist();
                                    } else {
                                        getMvpView().checkWalletIdFail(stringApiResponse.getMessage());
                                    }
                                } else {
                                    getMvpView().checkWalletIdFail(null);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                if (!isViewAttached()) {
                                    return;
                                }

                                // handle error here
                                if (e instanceof ANError) {
                                    ANError anError = (ANError) e;
                                    handleApiError(anError);
                                } else {
                                    getMvpView().checkWalletIdFail(null);
                                }
                            }
                        }));
    }

    private boolean isValidWalletId() {
        // Check for a valid wallet id.
        if (TextUtils.isEmpty(getMvpView().getWalletId())) {
            getMvpView().requireWalletId();
            return false;
        }

        if (!StringUtils.isValidIdStart(getMvpView().getWalletId())) {
            getMvpView().isValidIdStart();
            return false;
        }
        if (!StringUtils.isRequiredLength(getMvpView().getWalletId())) {
            getMvpView().requireWalletIdLength();
            return false;
        }
        if (!StringUtils.isWalletIdValid(getMvpView().getWalletId())) {
            getMvpView().invalidWalletId();
            return false;
        }
        return true;
    }
}
