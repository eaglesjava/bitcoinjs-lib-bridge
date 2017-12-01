package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/1.
 */
@PerActivity
public class SplashPresenter<M extends AppModel, V extends SplashMvpView> extends ModelPresenter<M, V> implements SplashMvpPresenter<M, V> {
    @Inject
    public SplashPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void checkBrowsed() {
        getMvpView().isGuideBrowsed(getModelManager().isGuideBrowsed());
    }
}
