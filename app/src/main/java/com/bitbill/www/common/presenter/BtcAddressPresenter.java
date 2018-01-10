package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.address.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.address.network.entity.RefreshAddressResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
public class BtcAddressPresenter<M extends AddressModel, V extends BtcAddressMvpView> extends ModelPresenter<M, V> implements BtcAddressMvpPresentder<M, V> {

    @Inject
    public BtcAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void refreshAddress() {
        if (!isValidWallet() || !isValidXPublicKey()) {
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        //刷新地址index+1
        long index = wallet.getLastAddressIndex() + 1;
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(wallet.getXPublicKey());
        getCompositeDisposable().add(getModelManager()
                .refreshAddress(new RefreshAddressRequest(xPublicKeyHash, index))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<RefreshAddressResponse>>() {
                    @Override
                    public void onNext(ApiResponse<RefreshAddressResponse> refreshAddressResponseApiResponse) {
                        super.onNext(refreshAddressResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (refreshAddressResponseApiResponse != null && refreshAddressResponseApiResponse.isSuccess()) {

                            if (refreshAddressResponseApiResponse.getData() != null) {
                                long indexNo = refreshAddressResponseApiResponse.getData().getIndexNo();
                                checkLastAddressIndex(indexNo, wallet);
                            }
                        } else {
                            getMvpView().newAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        } // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void checkLastAddressIndex(long indexNo, Wallet wallet) {
        // TODO: 2018/1/5 check index
        if (indexNo <= 0) {
            return;
        }
        // TODO: 2018/1/6  check wallet
        long lastIndex = wallet.getLastAddressIndex();
        if (StringUtils.isNotEmpty(wallet.getLastAddress())) {
            //如果最新地址已存在++
            lastIndex++;
        }
        if (indexNo < lastIndex) {
            getMvpView().reachAddressIndexLimit();
            String lastAddress = wallet.getLastAddress();
            //随机选择一个地址
            long randomIndex = ThreadLocalRandom.current().nextLong(indexNo);
            List<Address> addressList = wallet.getAddressList();
            if (!StringUtils.isEmpty(addressList)) {

                for (Address address : addressList) {
                    if (address.getIndex() == randomIndex) {
                        lastAddress = address.getName();
                        break;
                    }
                }
            }
            if (StringUtils.isNotEmpty(lastAddress)) {
                getMvpView().newAddressSuccess(lastAddress);
            } else {
                getMvpView().newAddressFail();
            }
        } else if (indexNo == lastIndex) {
            getBitcoinAddressByMasterXPublicKey(indexNo, wallet);
        } else {
            //批量生成地址
            getBitcoinContinuousAddress(lastIndex, indexNo, wallet);
        }
    }

    @Override
    public void getBitcoinAddressByMasterXPublicKey(long index, Wallet wallet) {
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(wallet.getXPublicKey(), index, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().newAddressFail();
                    return;
                }
                String address = jsResult[0];

                if (!isValidBtcAddress(address)) {
                    return;
                }
                wallet.setLastAddress(address);
                wallet.setLastAddressIndex(index);
                //更新地址index
                updateAddressIndex(wallet, jsResult);

            }
        });
    }

    @Override
    public void getBitcoinContinuousAddress(long fromIndex, long toIndex, Wallet wallet) {

        BitcoinJsWrapper.getInstance().getBitcoinContinuousAddressByMasterXPublicKey(wallet.getXPublicKey(), fromIndex, toIndex, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().newAddressFail();
                    return;
                }
                if (toIndex - fromIndex + 1 != jsResult.length) {
                    getMvpView().newAddressFail();
                    return;
                }
                String address = jsResult[jsResult.length - 1];

                if (!isValidBtcAddress(address)) {
                    return;
                }
                wallet.setLastAddressIndex(toIndex);
                wallet.setLastAddress(address);
                //更新地址index
                updateAddressIndex(wallet, jsResult);

            }
        });
    }

    @Override
    public void updateAddressIndex(Wallet wallet, String[] addressArray) {
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < addressArray.length; i++) {
            //构造address列表
            addressList.add(new Address(null, addressArray[i], wallet.getId(), wallet.getLastAddressIndex() - (addressArray.length - 1) + i, AppConstants.BTC_COIN_TYPE, new Date(), 0l));
        }
        getCompositeDisposable().add(getModelManager()
                .insertAddressListAndUpdatWallet(addressList, wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        //本地index更新成功
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aBoolean) {
                            getMvpView().newAddressSuccess(wallet.getLastAddress());
                        } else {
                            getMvpView().newAddressFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().newAddressFail();
                    }
                }));
    }


    private boolean isValidWallet() {
        if (getMvpView().getWallet() == null) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

    public boolean isValidXPublicKey() {
        if (StringUtils.isEmpty(getMvpView().getWallet().getXPublicKey())) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

    private boolean isValidBtcAddress(String btcAddress) {
        if (StringUtils.isEmpty(btcAddress)) {
            getMvpView().newAddressFail();
            return false;
        }
        return true;
    }

}
