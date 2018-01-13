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
import com.bitbill.www.di.scope.PerActivity;
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
@PerActivity
public class BtcAddressPresenter<M extends AddressModel, V extends BtcAddressMvpView> extends ModelPresenter<M, V> implements BtcAddressMvpPresentder<M, V> {

    @Inject
    public BtcAddressPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void loadAddress() {
        if (!isValidWallet()) {
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        //  默认地址初始化钱包时直接生产地址 只需直接获取即可
        if (StringUtils.isNotEmpty(wallet.getLastAddress())) {

            getMvpView().loadAddressSuccess(wallet.getLastAddress());
            return;
        }
        if (!isValidXPublicKey()) {
            return;
        }
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(wallet.getXPublicKey(), wallet.getLastAddressIndex(), new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (!isViewAttached()) {
                    return;
                }

                if (jsResult == null) {
                    getMvpView().loadAddressFail();
                    return;
                }
                String address = jsResult[0];

                if (StringUtils.isEmpty(address)) {
                    getMvpView().loadAddressFail();
                    return;
                }
                wallet.setLastAddress(address);

                updateAddressIndex(wallet, new String[]{address}, true);
            }
        });
    }

    @Override
    public void refreshAddress(int refreshCount) {
        if (!isValidWallet() || !isValidXPublicKey() || refreshCount <= 0) {
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        //刷新地址index+1
        long index = wallet.getLastAddressIndex() + refreshCount;
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(wallet.getXPublicKey());
        getCompositeDisposable().add(getModelManager()
                .refreshAddress(new RefreshAddressRequest(xPublicKeyHash, index))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<RefreshAddressResponse>>(getMvpView()) {
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
                            getMvpView().refreshAddressFail();
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

    public void checkLastAddressIndex(long indexNo, Wallet wallet) {
        // check index
        if (indexNo <= 0) {
            return;
        }
        //  check wallet
        if (wallet == null) {
            return;
        }
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
                getMvpView().refreshAddressSuccess(lastAddress);
            } else {
                getMvpView().refreshAddressFail();
            }
        } else if (indexNo == lastIndex) {
            getBitcoinAddressByMasterXPublicKey(indexNo, wallet);
        } else {
            //批量生成地址
            getBitcoinContinuousAddress(lastIndex, indexNo, wallet);
        }
    }

    public void getBitcoinAddressByMasterXPublicKey(long index, Wallet wallet) {
        getMvpView().showLoading();
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(wallet.getXPublicKey(), index, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().refreshAddressFail();
                    return;
                }
                String address = jsResult[0];

                if (StringUtils.isEmpty(address)) {
                    getMvpView().refreshAddressFail();
                    return;
                }
                wallet.setLastAddress(address);
                wallet.setLastAddressIndex(index);
                //更新地址index
                updateAddressIndex(wallet, jsResult, false);

            }
        });
    }

    public void getBitcoinContinuousAddress(long fromIndex, long toIndex, Wallet wallet) {
        getMvpView().showLoading();
        BitcoinJsWrapper.getInstance().getBitcoinContinuousAddressByMasterXPublicKey(wallet.getXPublicKey(), fromIndex, toIndex, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String... jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().hideLoading();
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().refreshAddressFail();
                    return;
                }
                if (toIndex - fromIndex + 1 != jsResult.length) {
                    getMvpView().refreshAddressFail();
                    return;
                }
                String address = jsResult[jsResult.length - 1];

                if (StringUtils.isEmpty(address)) {
                    getMvpView().refreshAddressFail();
                    return;
                }
                wallet.setLastAddressIndex(toIndex);
                wallet.setLastAddress(address);
                //更新地址index
                updateAddressIndex(wallet, jsResult, false);

            }
        });
    }

    public void updateAddressIndex(Wallet wallet, String[] addressArray, boolean isLoad) {
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < addressArray.length; i++) {
            //构造address列表
            addressList.add(new Address(null, addressArray[i], wallet.getId(), wallet.getLastAddressIndex() - (addressArray.length - 1) + i, AppConstants.BTC_COIN_TYPE, new Date(), 0l));
        }
        getCompositeDisposable().add(getModelManager()
                .insertAddressListAndUpdatWallet(addressList, wallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>(getMvpView()) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        //本地index更新成功
                        if (!isViewAttached()) {
                            return;
                        }
                        if (isLoad) {
                            if (aBoolean) {
                                getMvpView().loadAddressSuccess(wallet.getLastAddress());
                            } else {
                                getMvpView().loadAddressFail();
                            }
                        } else {
                            if (aBoolean) {
                                getMvpView().refreshAddressSuccess(wallet.getLastAddress());
                            } else {
                                getMvpView().refreshAddressFail();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().refreshAddressFail();
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


}
