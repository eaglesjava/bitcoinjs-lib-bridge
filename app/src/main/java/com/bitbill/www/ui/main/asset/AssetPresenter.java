package com.bitbill.www.ui.main.asset;

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
 * Created by isanwenyu@163.com on 2017/11/28.
 */
@PerActivity
public class AssetPresenter<M extends WalletModel, V extends AssetMvpView> extends ModelPresenter<M, V> implements AssetMvpPresenter<M, V> {
    @Inject
    public AssetPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
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
