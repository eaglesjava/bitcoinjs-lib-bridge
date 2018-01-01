package com.bitbill.www.ui.main.contact;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/26.
 */
@PerActivity
public class ContactPresenter<M extends ContactModel, V extends ContactMvpView> extends ModelPresenter<M, V> implements ContactMvpPresenter<M, V> {
    @Inject
    public ContactPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadContact() {
        getCompositeDisposable().add(getModelManager().getAllContacts()
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<Contact>>() {
                    @Override
                    public void onNext(List<Contact> contacts) {
                        super.onNext(contacts);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().loadContactSuccess(contacts);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().loadContactFail();
                    }
                }));
    }
}
