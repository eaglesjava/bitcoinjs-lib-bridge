package com.bitbill.www.ui.main.contact;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.contact.ContactModel;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.contact.network.entity.DeleteContactsRequest;
import com.bitbill.www.model.contact.network.entity.UpdateContactsRequest;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/1.
 */
@PerActivity
public class EditContactPresenter<M extends ContactModel, V extends EditContactMvpView> extends ModelPresenter<M, V> implements EditContactMvpPresenter<M, V> {

    @Inject
    public EditContactPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void updateContact() {
        if (!isValidContact() || !isValidContactName() || !isValidEdit()) {
            return;
        }
        Contact contact = getMvpView().getContact();
        contact.setContactName(getMvpView().getContactName());
        contact.setRemark(getMvpView().getRemark());
        getCompositeDisposable().add(getModelManager()
                .updateContacts(new UpdateContactsRequest(contact.getWalletId()
                        , getApp().getContactKey()
                        , (StringUtils.isEmpty(contact.getWalletId()) ? contact.getAddress() : null)
                        , contact.getRemark()
                        , contact.getContactName()
                        , contact.getCoinType()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<Void>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<Void> voidApiResponse) {
                        super.onNext(voidApiResponse);
                        if (handleApiResponse(voidApiResponse)) {
                            return;
                        }
                        if (voidApiResponse.isSuccess()) {
                            updateLocalContact();
                        } else {
                            getMvpView().updateContactFail(voidApiResponse.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().updateContactFail(null);
                        }
                    }
                }));

    }

    private void updateLocalContact() {
        Contact contact = getMvpView().getContact();

        getCompositeDisposable()
                .add(getModelManager()
                        .updateContact(contact)
                        .compose(this.applyScheduler())
                        .subscribeWith(new BaseSubcriber<Boolean>(getMvpView()) {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                super.onNext(aBoolean);
                                if (!isViewAttached()) {
                                    return;
                                }
                                if (aBoolean) {
                                    getMvpView().updateContactSuccess();
                                } else {
                                    getMvpView().updateContactFail(null);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                if (!isViewAttached()) {
                                    return;
                                }
                                getMvpView().updateContactFail(null);
                            }
                        }));
    }

    @Override
    public void deleteContact() {
        if (!isValidContact()) {
            return;
        }
        Contact contact = getMvpView().getContact();

        getCompositeDisposable().add(getModelManager()
                .deleteContacts(new DeleteContactsRequest(contact.getWalletId()
                        , getApp().getContactKey()
                        , (StringUtils.isEmpty(contact.getWalletId()) ? contact.getAddress() : null)))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<Void>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<Void> voidApiResponse) {
                        super.onNext(voidApiResponse);
                        if (handleApiResponse(voidApiResponse)) {
                            return;
                        }
                        if (voidApiResponse.isSuccess()) {
                            deleteLocalContact();
                        } else {
                            getMvpView().deleteContactFail(voidApiResponse.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().deleteContactFail(null);
                        }
                    }
                }));
    }

    private void deleteLocalContact() {

        Contact contact = getMvpView().getContact();
        getCompositeDisposable().add(getModelManager().deleteContact(contact)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>(getMvpView()) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {
                            getMvpView().deleteContactSuccess();
                        } else {
                            getMvpView().deleteContactFail(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().deleteContactFail(null);
                    }
                }));
    }

    private boolean isValidEdit() {
        Contact contact = getMvpView().getContact();
        if (StringUtils.equals(contact.getContactName(), getMvpView().getContactName()) && StringUtils.equals(contact.getRemark(), getMvpView().getRemark())) {
            //联系人内容没有改变
            getMvpView().contactNoEdit();
            return false;
        }
        return true;
    }

    private boolean isValidContact() {
        if (getMvpView().getContact() == null) {
            getMvpView().requireContact();
            return false;
        }
        return true;
    }

    private boolean isValidContactName() {
        if (StringUtils.isEmpty(getMvpView().getContactName())) {
            getMvpView().requireContactName();
            return false;
        }
        return true;
    }

}
