package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/1.
 */
@PerActivity
public class SplashPresenter<M extends WalletModel, V extends SplashMvpView> extends ModelPresenter<M, V> implements SplashMvpPresenter<M, V> {
    @Inject
    public SplashPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void hasWallet() {
        getCompositeDisposable().add(getModelManager().getWalletById(1l)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Wallet>() {
                    @Override
                    public void onNext(Wallet wallet) {
                        super.onNext(wallet);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hasWallet(wallet != null);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hasWallet(false);
                    }
                }));
    }
}
