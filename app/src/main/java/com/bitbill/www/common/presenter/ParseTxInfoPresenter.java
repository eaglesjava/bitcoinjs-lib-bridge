package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.wallet.network.entity.TxElement;
import com.bitbill.www.model.wallet.network.entity.TxItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
@PerActivity
public class ParseTxInfoPresenter<M extends AddressModel, V extends ParseTxInfoMvpView> extends ModelPresenter<M, V> implements ParseTxInfoMvpPresenter<M, V> {
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
                    List<TxItem> txItems = new ArrayList<>();
                    for (TxElement txElement : txInfos) {
                        List<TxElement.InputsBean> inputs = txElement.getInputs();

                        long inputAmount = 0;
                        boolean isAllIn = true;
                        TxItem.InOut inOut = TxItem.InOut.IN;
                        String inAddress = null;
                        for (TxElement.InputsBean input : inputs) {
                            Address addressByName = getModelManager().getAddressByName(input.getAddress());
                            if (addressByName != null) {
                                inputAmount += input.getValue();
                                if (inAddress != null) {
                                    inAddress = input.getAddress();
                                }
                            } else {
                                isAllIn = false;
                            }
                        }
                        long outAmount = 0;
                        boolean isAllOut = true;
                        String outAddress = null;
                        List<TxElement.OutputsBean> outputs = txElement.getOutputs();
                        for (TxElement.OutputsBean output : outputs) {
                            Address addressByName = getModelManager().getAddressByName(output.getAddress());
                            if (addressByName != null) {
                                outAmount += output.getValue();
                                if (outAddress != null) {
                                    outAddress = output.getAddress();
                                }
                            } else {
                                isAllOut = false;

                            }
                        }

                        TxItem txItem = new TxItem();
                        long amount = 0;
                        if (isAllIn && isAllOut) {
                            amount = outAmount;
                            //ouput地址全部是本地地址 为转移类型
                            inOut = TxItem.InOut.TRANSFER;
                            txItem.setGatherAddressOut(outputs.get(0).getAddress());
                        } else if (inputAmount > 0) {
                            //发送
                            inOut = TxItem.InOut.OUT;
                            amount = inputAmount;
                            txItem.setGatherAddressIn(inAddress);
                        } else {
                            //接收
                            inOut = TxItem.InOut.IN;
                            amount = outAmount;
                            txItem.setGatherAddressOut(outAddress);
                        }
                        txItem.setSumAmount(amount);
                        txItem.setInOut(inOut);
                        txItems.add(txItem);
                        txItem.setTxHash(txElement.getTxHash());
                        txItem.setHeight(txElement.getHeight());
                        txItem.setCreatedTime(txElement.getCreatedTime());
                    }
                    return Observable.just(txItems);
                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<List<TxItem>>() {
                    @Override
                    public void onNext(List<TxItem> txItems) {
                        super.onNext(txItems);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().parsedTxItemList(txItems);
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
