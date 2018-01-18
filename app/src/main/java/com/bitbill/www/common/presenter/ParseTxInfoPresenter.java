package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
@PerActivity
public class ParseTxInfoPresenter<M extends TxModel, V extends ParseTxInfoMvpView> extends ModelPresenter<M, V> implements ParseTxInfoMvpPresenter<M, V> {
    @Inject
    AddressModel mAddressModel;

    @Inject
    public ParseTxInfoPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void parseTxInfo() {
        if (!isValidTxInfoList()) {
            return;
        }
        List<TxElement> txInfoList = getMvpView().getTxInfoList();

        getCompositeDisposable().add(Observable.just(txInfoList)
                .concatMap(txInfos -> {
                    List<TxRecord> txRecords = new ArrayList<>();
                    for (TxElement txElement : txInfos) {
                        List<TxElement.InputsBean> inputs = txElement.getInputs();

                        long inputAmount = 0;
                        TxRecord.InOut inOut = TxRecord.InOut.IN;
                        Long inWalletId = null;
                        List<String> inSelfAddressList = new ArrayList<>();
                        for (TxElement.InputsBean input : inputs) {
                            Address addressByName = mAddressModel.getAddressByName(input.getAddress());
                            if (addressByName != null) {
                                inputAmount += input.getValue();
                                inSelfAddressList.add(input.getAddress());
                                if (inWalletId == null) {
                                    inWalletId = addressByName.getWalletId();
                                }
                            }
                        }
                        long outSelfAmout = 0;
                        long outOtherAmout = 0;
                        Long outWalletId = null;
                        List<String> outSelfAddressList = new ArrayList<>();
                        List<String> outOtherAddressList = new ArrayList<>();
                        List<TxElement.OutputsBean> outputs = txElement.getOutputs();
                        for (TxElement.OutputsBean output : outputs) {
                            Address addressByName = mAddressModel.getAddressByName(output.getAddress());
                            if (addressByName != null) {
                                outSelfAmout += output.getValue();
                                outSelfAddressList.add(output.getAddress());
                                if (outWalletId == null) {
                                    outWalletId = addressByName.getWalletId();
                                }
                            } else {
                                outOtherAmout += output.getValue();
                                outOtherAddressList.add(output.getAddress());
                            }
                        }

                        TxRecord txRecord = new TxRecord();
                        long amount = 0;
                        boolean isContainIn = inSelfAddressList.size() > 0;
                        boolean isAllOut = outputs.size() == outSelfAddressList.size();
                        boolean isContainOut = outSelfAddressList.size() > 0;
                        if (isContainIn && isAllOut) {
                            amount = outSelfAmout;
                            //ouput地址全部是本地地址 为转移类型
                            inOut = TxRecord.InOut.TRANSFER;
                        } else if (isContainIn) {
                            //发送
                            inOut = TxRecord.InOut.OUT;
                            amount = outOtherAmout;
                        } else if (isContainOut) {
                            //接收
                            inOut = TxRecord.InOut.IN;
                            amount = outSelfAmout;
                        } else {
                            continue;
                        }
                        if (outWalletId != null) {
                            txRecord.setWalletId(outWalletId);
                        } else if (inWalletId != null) {
                            txRecord.setWalletId(inWalletId);
                        }
                        txRecord.setSumAmount(amount);
                        txRecord.setInOut(inOut);
                        txRecord.setTxHash(txElement.getTxHash());
                        txRecord.setHeight(txElement.getHeight());
                        txRecord.setCreatedTime(StringUtils.getDate(txElement.getCreatedTime()));
                        txRecord.setRemark(txElement.getRemark());
                        txRecord.setElementId(txElement.getId());
                        getModelManager().insertTxRecordAndInputsOutputs(txRecord, inputs, outputs);
                        txRecords.add(txRecord);
                    }
                    return Observable.just(txRecords);
                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<TxRecord>>() {
                    @Override
                    public void onNext(List<TxRecord> txRecords) {
                        super.onNext(txRecords);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().parsedTxItemList(txRecords);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().parsedTxItemListFail();
                    }
                }));


    }


    public boolean isValidTxInfoList() {
        List<TxElement> txInfoList = getMvpView().getTxInfoList();
        if (StringUtils.isEmpty(txInfoList)) {
            getMvpView().requireTxInfoList();
            return false;
        }

        for (TxElement txInfo : txInfoList) {
            List<TxElement.InputsBean> inputs = txInfo.getInputs();
            if (StringUtils.isEmpty(inputs)) {
                getMvpView().getTxInfoListFail();
                return false;
            }
            List<TxElement.OutputsBean> outputs = txInfo.getOutputs();
            if (StringUtils.isEmpty(outputs)) {
                getMvpView().getTxInfoListFail();
                return false;
            }
        }
        return true;
    }
}
