package com.bitbill.www.ui.main.contact;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/25.
 */
@PerActivity
public class AddContactByIdPresenter<M extends WalletModel, V extends AddContactByIdMvpView> extends ModelPresenter<M, V> implements AddContactByIdMvpPresenter<M, V> {

    @Inject
    public AddContactByIdPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void searchWalletId() {
        if (!isValidWalletId()) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .searchWalletId(new SearchWalletIdRequest(getMvpView().getWalletId()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<SearchWalletIdResponse>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<SearchWalletIdResponse> searchWalletIdResponseApiResponse) {
                        super.onNext(searchWalletIdResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (searchWalletIdResponseApiResponse != null && searchWalletIdResponseApiResponse.isSuccess()) {
                            getMvpView().searchWalletIdSuccess();
                        } else {
                            getMvpView().searchWalletIdFail();
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

                            getMvpView().searchWalletIdFail();
                        }

                    }
                }));

    }

    public boolean isValidWalletId() {
        if (StringUtils.isEmpty(getMvpView().getWalletId())) {
            getMvpView().requireWalletId();
            return false;
        }
        return true;
    }
}
