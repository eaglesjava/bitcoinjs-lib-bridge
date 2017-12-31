package com.bitbill.www.ui.main.my;

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
import com.bitbill.www.model.contact.network.entity.RecoverContactsRequest;
import com.bitbill.www.model.contact.network.entity.RecoverContactsResponse;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/29.
 */
@PerActivity
public class ContactSettingPresenter<M extends ContactModel, V extends ContactSettingMvpView> extends ModelPresenter<M, V> implements ContactSettingMvpPresenter<M, V> {
    @Inject
    public ContactSettingPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public String getWalletKey() {
        return DeviceUtil.getDeviceId();
    }

    @Override
    public void recoverContact(String contactKey) {
        if (!isValidContactKey(contactKey)) {
            return;
        }
        getCompositeDisposable()
                .add(getModelManager()
                        .recoverContacts(new RecoverContactsRequest(getWalletKey(), contactKey))
                        .compose(this.applyScheduler())
                        .subscribeWith(new BaseSubcriber<ApiResponse<RecoverContactsResponse>>(getMvpView()) {
                            @Override
                            public void onNext(ApiResponse<RecoverContactsResponse> recoverContactsResponseApiResponse) {
                                super.onNext(recoverContactsResponseApiResponse);
                                if (!isViewAttached()) {
                                    return;
                                }
                                if (recoverContactsResponseApiResponse != null && recoverContactsResponseApiResponse.isSuccess()) {
                                    if (recoverContactsResponseApiResponse.getData() != null) {
                                        List<RecoverContactsResponse.ContactsBean> contacts = recoverContactsResponseApiResponse.getData().getContacts();
                                        if (!StringUtils.isEmpty(contacts)) {
                                            insertContact(contacts);
                                        } else {
                                            getMvpView().receoverContactsNull();
                                        }
                                    } else {
                                        getMvpView().receoverContactFail();
                                    }
                                } else {
                                    getMvpView().receoverContactFail();
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
                                    getMvpView().receoverContactFail();
                                }
                            }
                        }));
    }

    @Override
    public void insertContact(List<RecoverContactsResponse.ContactsBean> contacts) {

        getCompositeDisposable().add(Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                boolean isSuccess = false;
                for (RecoverContactsResponse.ContactsBean contactsBean : contacts) {
                    getModelManager().insertContact(new Contact(null
                            , contactsBean.getWalletContact()
                            , getWalletKey()
                            , contactsBean.getWalletAddress()
                            , contactsBean.getRemark()
                            , contactsBean.getContactName()
                            , AppConstants.BTC_COIN_TYPE));
                }
                isSuccess = true;
                return isSuccess;
            }
        })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {
                            getMvpView().recoverContactSuccess();
                        } else {
                            getMvpView().receoverContactFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().receoverContactFail();
                    }
                }));
    }

    public boolean isValidContactKey(String contactKey) {
        if (StringUtils.isEmpty(contactKey)) {
            getMvpView().requireContactKey();
            return false;
        }
        return true;
    }
}
