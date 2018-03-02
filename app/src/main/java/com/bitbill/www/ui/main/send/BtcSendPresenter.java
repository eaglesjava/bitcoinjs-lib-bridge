package com.bitbill.www.ui.main.send;

import com.androidnetworking.error.ANError;
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
 * Created by isanwenyu on 2017/12/20.
 */
@PerActivity
public class BtcSendPresenter<M extends ContactModel, V extends BtcSendMvpView> extends ModelPresenter<M, V> implements BtcSendMvpPresenter<M, V> {

    @Inject
    public BtcSendPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
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
                .subscribeWith(new BaseSubcriber<ApiResponse<GetLastAddressResponse>>() {
                    @Override
                    public void onNext(ApiResponse<GetLastAddressResponse> getLastAddressResponseApiResponse) {
                        super.onNext(getLastAddressResponseApiResponse);
                        if (handleApiResponse(getLastAddressResponseApiResponse)) {
                            return;
                        }
                        if (getLastAddressResponseApiResponse.isSuccess() && getLastAddressResponseApiResponse.getData() != null) {
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
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().getLastAddressFail();
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

    @Override
    public void updateContact() {
        getCompositeDisposable().add(getModelManager()
                .updateContact(getMvpView().getSendContact())
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

}
