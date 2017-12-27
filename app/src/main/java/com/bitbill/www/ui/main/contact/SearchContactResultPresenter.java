package com.bitbill.www.ui.main.contact;

import com.androidnetworking.error.ANError;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.contact.network.entity.AddContactsRequest;
import com.bitbill.www.model.contact.network.entity.AddContactsResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/26.
 */
@PerActivity
public class SearchContactResultPresenter<M extends ContactModel, V extends SearchContactResultMvpView> extends ModelPresenter<M, V> implements SearchContactResultMvpPresenter<M, V> {

    @Inject
    public SearchContactResultPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void checkContact() {

        if (!isValidWalletId()) {
            return;

        }
        //check 本地是否已存在
        getCompositeDisposable().add(getModelManager()
                .getContactByWalletId(getMvpView().getWalletId())
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Contact>() {
                    @Override
                    public void onNext(Contact contact) {
                        super.onNext(contact);
                        if (contact != null) {
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().isExsistContact();
                        } else {
                            addContact();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        addContact();
                    }
                }));
    }

    @Override
    public void addContact() {
        if (!isValidWalletId() || !isValidContactName()) {
            return;

        }
        Contact contact = new Contact(null, getMvpView().getWalletId(), StringUtils.getContactKey(), getMvpView().getAddress(), getMvpView().getRemark(), getMvpView().getContactName(), AppConstants.BTC_COIN_TYPE);
        getCompositeDisposable()
                .add(getModelManager()
                        .insertContact(contact)
                        .concatMap(aLong -> {
                            contact.setId(aLong);
                            return getModelManager()
                                    .addContacts(new AddContactsRequest(contact.getWalletId(), contact.getWalletKey(), contact.getAddress(), contact.getRemark(), contact.getContactName(), contact.getCoinType()));
                        })
                        .compose(this.applyScheduler())
                        .subscribeWith(new BaseSubcriber<ApiResponse<AddContactsResponse>>(getMvpView()) {
                            @Override
                            public void onNext(ApiResponse<AddContactsResponse> addContactsResponseApiResponse) {
                                super.onNext(addContactsResponseApiResponse);
                                if (addContactsResponseApiResponse != null) {
                                    if (addContactsResponseApiResponse.isSuccess()) {
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        getMvpView().addContactSuccess();
                                    } else {
                                        removeContact(contact);
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        getMvpView().addContactFail(addContactsResponseApiResponse.getMessage());

                                    }
                                } else {
                                    removeContact(contact);
                                    if (!isViewAttached()) {
                                        return;
                                    }

                                    getMvpView().addContactFail(null);
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                removeContact(contact);
                                if (!isViewAttached()) {
                                    return;
                                }
                                if (e instanceof ANError) {
                                    handleApiError(((ANError) e));
                                } else {
                                    getMvpView().addContactFail(null);
                                }
                            }
                        }));

    }

    private void removeContact(Contact contact) {
        if (contact == null) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .deleteContact(contact)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Void>() {
                    @Override
                    public void onNext(Void aVoid) {
                        super.onNext(aVoid);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

    }

    public boolean isValidWalletId() {

        if (StringUtils.isEmpty(getMvpView().getWalletId())) {
            getMvpView().requireWalletId();
            return false;
        }
        return true;
    }

    public boolean isValidContactName() {
        if (StringUtils.isEmpty(getMvpView().getContactName())) {
            getMvpView().requireContactName();
            return false;
        }
        return true;
    }
}
