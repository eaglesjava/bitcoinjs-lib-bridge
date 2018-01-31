package com.bitbill.www.ui.main.my;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.app.network.entity.FeeBackRequest;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/31.
 */
@PerActivity
public class FeebackPresenter<M extends AppModel, V extends FeebackMvpView> extends ModelPresenter<M, V> implements FeebackMvpPresenter<M, V> {
    @Inject
    public FeebackPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void sendFeeback() {
        if (!isValidContent() || !isValidContact()) {
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .feeBack(new FeeBackRequest(getMvpView().getContent(), getMvpView().getContact()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        super.onNext(apiResponse);
                        if (handleApiResponse(apiResponse)) {
                            return;
                        }
                        if (apiResponse.isSuccess()) {
                            getMvpView().sendFeebackSuccess();
                        } else {
                            getMvpView().sendFeebackFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        if (e instanceof ANError) {
                            handleApiError(((ANError) e));
                        } else {
                            getMvpView().sendFeebackFail();
                        }
                    }
                }));
    }

    public boolean isValidContent() {
        if (StringUtils.isEmpty(getMvpView().getContent())) {
            getMvpView().requireContent();
            return false;
        }
        if (getMvpView().getContent().length() > 200) {
            getMvpView().tooMuchWords();
            return false;
        }
        return true;
    }

    public boolean isValidContact() {
        if (StringUtils.isEmpty(getMvpView().getContact())) {
            getMvpView().requireContact();
            return false;
        }
        if (getMvpView().getContact().length() > 200) {
            getMvpView().tooMuchWords();
            return false;
        }
        return true;
    }
}
