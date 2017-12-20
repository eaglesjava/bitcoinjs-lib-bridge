package com.bitbill.www.ui.main.send;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/20.
 */
@PerActivity
public class BtcSendPresenter<M extends WalletModel, V extends BtcSendMvpView> extends ModelPresenter<M, V> implements BtcSendMvpPresenter<M, V> {


    @Inject
    public BtcSendPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void validateBtcAddress() {
        if (!isValidAddress()) {
            return;
        }
        BitcoinJsWrapper.getInstance().validateAddress(getMvpView().getSendAddress(), (key, jsResult) -> {
            if (!isViewAttached()) {
                return;
            }
            if (StringUtils.isEmpty(jsResult)) {
                getMvpView().validateAddress(false);
                return;
            }
            if ("true".equals(jsResult[0])) {
                //验证成功
                getMvpView().validateAddress(true);
            } else {
                getMvpView().validateAddress(false);
            }
        });
    }

    public boolean isValidAddress() {
        if (StringUtils.isEmpty(getMvpView().getSendAddress())) {
            getMvpView().requireAddress();
            return false;
        }
        return true;
    }
}
