package com.bitbill.www.common.presenter;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.JsResult;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
@PerActivity
public class SyncAddressPresenter<M extends AddressModel, V extends SyncAddressMvpView> extends ModelPresenter<M, V> implements SyncAddressMvpPresentder<M, V> {

    @Inject
    public SyncAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        //do nothing
    }

    @Override
    public void onDetach() {
        //do nothing
    }

    @Override
    public void syncLastAddressIndex(long indexNo, long changeIndexNo, Wallet wallet) {
        //  check index
        if (indexNo <= 0) {
            return;
        }
        //   check wallet
        if (wallet == null) {
            return;
        }
        long lastIndex = wallet.getLastAddressIndex();
        if (indexNo > lastIndex) {
            //批量生成地址
            getBitcoinContinuousAddress(lastIndex, indexNo, wallet, false);
        }
        long lastChangeAddressIndex = wallet.getLastChangeAddressIndex();
        if (changeIndexNo > lastChangeAddressIndex) {
            //批量生成地址
            getBitcoinContinuousAddress(lastChangeAddressIndex, changeIndexNo, wallet, true);
        }
    }

    public void getBitcoinContinuousAddress(long fromIndex, long toIndex, Wallet wallet, boolean isInternal) {
        String publicKey = isInternal ? wallet.getInternalPublicKey() : wallet.getExtentedPublicKey();
        BitcoinJsWrapper.getInstance().getBitcoinContinuousAddressByMasterXPublicKey(publicKey, fromIndex, toIndex, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String jsResult) {

                if (StringUtils.isEmpty(jsResult)) {
                    return;
                }
                JsResult result = null;
                try {
                    result = JsonUtils.deserialize(jsResult, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    return;
                }
                if (result.status == JsResult.STATUS_SUCCESS) {
                    String[] data = result.getData();
                    if (StringUtils.isEmpty(data)) {
                        return;
                    }
                    if (toIndex - fromIndex + 1 != data.length) {
                        return;
                    }
                    String address = data[data.length - 1];

                    if (!isValidBtcAddress(address)) {
                        return;
                    }
                    if (isInternal) {
                        wallet.setLastChangeAddressIndex(toIndex);
                        wallet.setLastChangeAddress(address);
                    } else {
                        wallet.setLastAddressIndex(toIndex);
                        wallet.setLastAddress(address);
                    }
                    //更新地址index
                    updateAddressIndex(wallet, data, isInternal);
                }
            }
        });
    }

    public void updateAddressIndex(Wallet wallet, String[] addressArray, boolean isInternal) {
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < addressArray.length; i++) {
            //构造address列表
            long index = (isInternal ? wallet.getLastChangeAddressIndex() : wallet.getLastAddressIndex()) - (addressArray.length - 1) + i;
            addressList.add(new Address(null, addressArray[i], wallet.getId(), index, AppConstants.BTC_COIN_TYPE, new Date(), 0l, isInternal));
        }
        getCompositeDisposable().add(getModelManager()
                .insertAddressListAndUpdatWallet(addressList, wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>(getMvpView()) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        //本地index更新成功
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private boolean isValidBtcAddress(String btcAddress) {
        return !StringUtils.isEmpty(btcAddress);
    }

}
