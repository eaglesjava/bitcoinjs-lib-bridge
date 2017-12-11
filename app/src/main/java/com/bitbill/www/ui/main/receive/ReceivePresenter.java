package com.bitbill.www.ui.main.receive;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/11.
 */
@PerActivity
public class ReceivePresenter<M extends AppModel, V extends ReceiveMvpView> extends ModelPresenter<M, V> implements ReceiveMvpPresenter<M, V> {
    @Inject
    public ReceivePresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void setRemindDialogShown() {
        getModelManager().setReceiveRemindDialogShown();
    }

    @Override
    public boolean isRemindDialogShown() {
        return getModelManager().isReceiveRemindDialogShown();
    }
}
