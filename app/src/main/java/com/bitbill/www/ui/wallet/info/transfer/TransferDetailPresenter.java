package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.transaction.db.entity.Input;
import com.bitbill.www.model.transaction.db.entity.Output;
import com.bitbill.www.model.transaction.db.entity.TxRecord;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

/**
 * Created by isanwenyu on 2018/1/30.
 */
@PerActivity
public class TransferDetailPresenter<M extends AddressModel, V extends TransferDetailMvpView> extends ModelPresenter<M, V> implements TransferDetailMvpPresenter<M, V> {
    @Inject
    public TransferDetailPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void buidTransferData() {
        if (!isValidTxRecord()) {
            return;
        }
        TxRecord txRecord = getMvpView().getTxRecord();
        getCompositeDisposable().add(Observable.just(txRecord)
                .map(new Function<TxRecord, TxRecord>() {
                    @Override
                    public TxRecord apply(TxRecord txRecord) throws Exception {
                        for (Input inputsBean : txRecord.getInputs()) {
                            //解析地址
                            Address addressByName = getModelManager().getAddressByName(inputsBean.getAddress());
                            if (addressByName != null) {
                                inputsBean.setMine(true);
                                inputsBean.setInternal(addressByName.getIsInternal());
                            }
                        }
                        for (Output outputsBean : txRecord.getOutputs()) { //解析地址
                            Address addressByName = getModelManager().getAddressByName(outputsBean.getAddress());
                            if (addressByName != null) {
                                outputsBean.setMine(true);
                                outputsBean.setInternal(addressByName.getIsInternal());
                            }
                        }
                        return txRecord;
                    }
                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<TxRecord>() {
                    @Override
                    public void onNext(TxRecord txRecord) {
                        super.onNext(txRecord);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().buildDataSuccess(txRecord);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().buildDataFail();
                    }
                }));


    }

    public boolean isValidTxRecord() {
        if (getMvpView().getTxRecord() == null) {
            getMvpView().buildDataFail();
            return false;
        }
        return true;
    }
}
