package com.bitbill.www.ui.common;

import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressRequest;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/26.
 */
@PerActivity
public class GetLastAddressPresenter<M extends ContactModel, V extends GetLastAddressMvpView> extends ModelPresenter<M, V> implements GetLastAddressMvpPresenter<M, V> {
    @Inject
    public GetLastAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getLastAddress() {
        if (!isValidWalletId()) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .getLastAddress(new GetLastAddressRequest(getMvpView().getWalletId()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<GetLastAddressResponse>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<GetLastAddressResponse> getLastAddressResponseApiResponse) {
                        super.onNext(getLastAddressResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (getLastAddressResponseApiResponse != null && getLastAddressResponseApiResponse.isSuccess() && getLastAddressResponseApiResponse.getData() != null) {
                            getMvpView().getLastAddressSuccess(getLastAddressResponseApiResponse.getData().getAddress());

                        } else {
                            getMvpView().getLastAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().getLastAddressFail();
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
