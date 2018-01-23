package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.JsResult;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.google.gson.JsonSyntaxException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/26.
 */
@PerActivity
public class ValidateAddressPresenter<M extends AddressModel, V extends ValidateAddressMvpView> extends ModelPresenter<M, V> implements ValidateAddressMvpPresenter<M, V> {
    @Inject
    public ValidateAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void validateBtcAddress() {
        if (!isValidAddress()) {
            return;
        }
        BitcoinJsWrapper.getInstance().validateAddress(getMvpView().getAddress(), (key, jsResult) -> {
            if (!isViewAttached()) {
                return;
            }
            if (StringUtils.isEmpty(jsResult)) {
                getMvpView().validateAddress(false);
                return;
            }
            JsResult result = null;
            try {
                result = JsonUtils.deserialize(jsResult, JsResult.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (result == null) {
                getMvpView().validateAddress(false);
                return;
            }
            if (result.status == JsResult.STATUS_SUCCESS) {
                String[] data = result.getData();
                if (data != null && "true".equals(data[0])) {
                    //验证成功
                    getMvpView().validateAddress(true);
                } else {
                    getMvpView().validateAddress(false);
                }
            } else {
                getMvpView().validateAddress(false);
            }
        });
    }

    public boolean isValidAddress() {
        if (StringUtils.isEmpty(getMvpView().getAddress())) {
            getMvpView().requireAddress();
            return false;
        }
        return true;
    }
}
