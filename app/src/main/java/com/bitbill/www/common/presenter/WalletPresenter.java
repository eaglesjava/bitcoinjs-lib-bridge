package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/28.
 */
@PerActivity
public class WalletPresenter<M extends WalletModel, V extends WalletMvpView> extends ModelPresenter<M, V> implements WalletMvpPresenter<M, V> {
    @Inject
    public WalletPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadWallets() {
        getCompositeDisposable().add(getModelManager().getAllWallets()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<Wallet>>() {
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
                        getMvpView().loadWalletsSuccess(wallets);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError((ANError) e);
                        } else {
                            getMvpView().loadWalletsFail();
                        }
                    }
                }));
    }


}
