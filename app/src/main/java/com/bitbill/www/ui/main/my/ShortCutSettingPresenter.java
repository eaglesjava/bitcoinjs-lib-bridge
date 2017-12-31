package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/31.
 */
@PerActivity
public class ShortCutSettingPresenter<M extends AppModel, V extends MvpView> extends ModelPresenter<M, V> implements ShortCutSettingMvpPresenter<M, V> {
    @Inject
    public ShortCutSettingPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public boolean isShortcutShown() {
        return getModelManager().isShortcutShown();
    }

    @Override
    public void setShortcutShown(boolean shown) {
        getModelManager().setShortcutShown(shown);
    }
}
