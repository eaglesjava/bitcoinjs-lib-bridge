package com.bitbill.www.ui.main.contact;

import com.androidnetworking.error.ANError;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.contact.network.entity.AddContactsRequest;
import com.bitbill.www.model.contact.network.entity.AddContactsResponse;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/27.
 */
@PerActivity
public class AddContactByAddressPresenter<M extends ContactModel, V extends AddContactByAddressMvpView> extends ModelPresenter<M, V> implements AddContactByAddressMvpPresenter<M, V> {

    @Inject
    public AddContactByAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void checkContact() {


        if (!isValidContactName() || !isValidAddress()) {
            return;

        }
        getCompositeDisposable().add(getModelManager().getContactByAddress(getMvpView().getAddress())
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
                        addContact();
                    }
                }));
    }

    @Override
    public void addContact() {
        Contact contact = new Contact(null
                , null
                , DeviceUtil.getDeviceId()
                , getMvpView().getAddress()
                , getMvpView().getRemark()
                , getMvpView().getContactName()
                , AppConstants.BTC_COIN_TYPE);
        getCompositeDisposable().add(getModelManager()
                .insertContact(contact)
                .concatMap(aLong -> getModelManager()
                        .addContacts(new AddContactsRequest(contact.getWalletId(), contact.getWalletKey(), contact.getAddress(), contact.getRemark(), contact.getContactName(), contact.getCoinType())))
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
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                })
        );

    }

    public boolean isValidAddress() {
        if (StringUtils.isEmpty(getMvpView().getAddress())) {
            getMvpView().requireAddress();
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
