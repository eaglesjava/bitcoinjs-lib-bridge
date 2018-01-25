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
        getCompositeDisposable().add(Observable.just(StringUtils.removeDuplicateList(txInfoList))
                .concatMap(txInfos -> {
                    List<TxRecord> txRecords = new ArrayList<>();
                    for (TxElement txElement : txInfos) {
                        List<TxElement.InputsBean> inputs = txElement.getInputs();

                        List<Long> inWalletIdList = new ArrayList<>();

                        for (TxElement.InputsBean input : inputs) {
                            Address addressByName = mAddressModel.getAddressByName(input.getAddress());
                            if (addressByName != null) {
                                Long walletId = addressByName.getWalletId();
                                if (!inWalletIdList.contains(walletId)) {
                                    inWalletIdList.add(walletId);
                                }
                            }
                        }
                        List<Long> outWalletIdList = new ArrayList<>();
                        List<TxElement.OutputsBean> outputs = txElement.getOutputs();
                        int foundAddressCount = 0;
                        for (TxElement.OutputsBean output : outputs) {
                            Address addressByName = mAddressModel.getAddressByName(output.getAddress());
                            if (addressByName != null) {
                                foundAddressCount++;
                                Long walletId = addressByName.getWalletId();
                                //排除in中存在的waleltID
                                if (!outWalletIdList.contains(walletId)) {
                                    outWalletIdList.add(walletId);
                                }
                            }
                        }

                        boolean isContainIn = inWalletIdList.size() > 0;
                        boolean isAllOut = outWalletIdList.size() == 1 && foundAddressCount == outputs.size();

                        //for in wallet
                        for (Long inWalletId : inWalletIdList) {
                            TxRecord inTxRecord = new TxRecord();
                            TxRecord.InOut inOut;
                            long amount = 0;
                            if (isAllOut && inWalletId.equals(outWalletIdList.get(0))) {
                                //ouput地址全部是本地地址 为转移类型
                                inOut = TxRecord.InOut.TRANSFER;
                                for (TxElement.OutputsBean output : outputs) {
                                    amount += output.getValue();
                                }

                            } else {
                                //发送
                                inOut = TxRecord.InOut.OUT;
                                for (TxElement.OutputsBean output : outputs) {
                                    Address addressByName = mAddressModel.getAddressByName(output.getAddress());
                                    if (addressByName == null || !inWalletId.equals(addressByName.getWalletId()) || !addressByName.getIsInternal()) {
                                        amount += output.getValue();
                                    }
                                }
                            }
                            inTxRecord.setWalletId(inWalletId);
                            inTxRecord.setSumAmount(amount);
                            inTxRecord.setInOut(inOut);
                            inTxRecord.setTxHash(txElement.getTxHash());
                            inTxRecord.setHeight(txElement.getHeight());
                            inTxRecord.setCreatedTime(StringUtils.getDate(txElement.getCreatedTime()));
                            inTxRecord.setRemark(txElement.getRemark());
                            inTxRecord.setElementId(txElement.getId());
                            getModelManager().insertTxRecordAndInputsOutputs(inTxRecord, inputs, outputs);
                            txRecords.add(inTxRecord);
                        }
                        if (isContainIn && isAllOut && inWalletIdList.get(0).equals(outWalletIdList.get(0))) {
                            //ouput地址全部是本地地址 为转移类型 插入一条记录
                            continue;
                        }
                        //for out wallet
                        for (Long outWalletId : outWalletIdList) {
                            if (inWalletIdList.contains(outWalletId)) {
                                //排除in中存在的waleltID
                                continue;
                            }
                            TxRecord outTxRecord = new TxRecord();
                            TxRecord.InOut inout = TxRecord.InOut.IN;
                            long outAmount = 0;
                            //接收
                            inout = TxRecord.InOut.IN;
                            for (TxElement.OutputsBean output : outputs) {
                                Address addressByName = mAddressModel.getAddressByName(output.getAddress());
                                if (addressByName != null && outWalletId.equals(addressByName.getWalletId())) {
                                    outAmount += output.getValue();
                                }
                            }
                            outTxRecord.setWalletId(outWalletId);
                            outTxRecord.setSumAmount(outAmount);
                            outTxRecord.setInOut(inout);
                            outTxRecord.setTxHash(txElement.getTxHash());
                            outTxRecord.setHeight(txElement.getHeight());
                            outTxRecord.setCreatedTime(StringUtils.getDate(txElement.getCreatedTime()));
                            outTxRecord.setRemark(txElement.getRemark());
                            outTxRecord.setElementId(txElement.getId());
                            getModelManager().insertTxRecordAndInputsOutputs(outTxRecord, inputs, outputs);
                            txRecords.add(outTxRecord);

                        }
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
