package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.JsResult;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.address.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.address.network.entity.RefreshAddressResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.google.gson.JsonSyntaxException;

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
        if (!isValidPublicKey()) {
            return;
        }
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(wallet.getExtentedPublicKey(), wallet.getLastAddressIndex(), new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().loadAddressFail();
                    return;
                }
                JsResult result = null;
                try {
                    result = JsonUtils.deserialize(jsResult, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    getMvpView().loadAddressFail();
                    return;
                }
                if (result.status == JsResult.STATUS_SUCCESS) {
                    String[] data = result.getData();
                    if (data == null) {
                        getMvpView().loadAddressFail();
                        return;
                    }
                    String address = data[0];

                    if (StringUtils.isEmpty(address)) {
                        getMvpView().loadAddressFail();
                        return;
                    }
                    wallet.setLastAddress(address);

                    updateAddressIndex(wallet, new String[]{address}, true, false);
                } else {
                    getMvpView().loadAddressFail();
                }
            }
        });
    }

    @Override
    public void refreshAddress(int refreshCount, int option) {
        if (!isValidWallet() || !isValidPublicKey() || refreshCount <= 0) {
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        int extendedRefreshCount = 0;
        int internalRefreshCount = 0;
        switch (option) {
            case -1:
                extendedRefreshCount = refreshCount;
                internalRefreshCount = refreshCount;
                break;
            case 0:
                extendedRefreshCount = refreshCount;
                break;
            case 1:
                internalRefreshCount = refreshCount;
                break;
        }
        //刷新地址index+refreshCount
        long refreshIndex = wallet.getLastAddressIndex() + extendedRefreshCount;
        long refreshChangeIndex = wallet.getLastChangeAddressIndex() + internalRefreshCount;
        String xPublicKeyHash = EncryptUtils.encryptMD5ToString(wallet.getExtentedPublicKey());
        getCompositeDisposable().add(getModelManager()
                .refreshAddress(new RefreshAddressRequest(xPublicKeyHash, refreshIndex, refreshChangeIndex))
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
                                long changeIndexNo = refreshAddressResponseApiResponse.getData().getChangeIndexNo();

                                // TODO: 2018/1/23 优化index
                                switch (option) {
                                    case -1:
                                        checkLastAddressIndex(indexNo, wallet, false);
                                        checkLastAddressIndex(changeIndexNo, wallet, true);
                                        break;
                                    case 0:
                                        checkLastAddressIndex(indexNo, wallet, false);
                                        break;
                                    case 1:
                                        checkLastAddressIndex(changeIndexNo, wallet, true);
                                        break;
                                }
                            }
                        } else {
                            getMvpView().refreshAddressFail(option == 1);
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

    public void checkLastAddressIndex(long indexNo, Wallet wallet, boolean isInternal) {
        // check index
        if (indexNo <= 0) {
            return;
        }
        //  check wallet
        if (wallet == null) {
            return;
        }
        long lastIndex = isInternal ? wallet.getLastChangeAddressIndex() : wallet.getLastAddressIndex();
        String lastAddress = isInternal ? wallet.getLastChangeAddress() : wallet.getLastAddress();
        if (StringUtils.isNotEmpty(lastAddress)) {
            //如果最新地址已存在++
            lastIndex++;
        }
        if (indexNo < lastIndex) {
            getMvpView().reachAddressIndexLimit();
            //随机选择一个地址
            long randomIndex = ThreadLocalRandom.current().nextLong(indexNo);
            List<Address> addressList = wallet.getAddressList();
            if (!StringUtils.isEmpty(addressList)) {

                for (Address address : addressList) {
                    if (isInternal == address.getIsInternal() && address.getIndex() == randomIndex) {
                        lastAddress = address.getName();
                        break;
                    }
                }
            }
            if (StringUtils.isNotEmpty(lastAddress)) {
                getMvpView().refreshAddressSuccess(lastAddress, isInternal);
            } else {
                getMvpView().refreshAddressFail(isInternal);
            }
        } else if (indexNo == lastIndex) {
            getBitcoinAddressByMasterXPublicKey(indexNo, wallet, isInternal);
        } else {
            //批量生成地址
            getBitcoinContinuousAddress(lastIndex, indexNo, wallet, isInternal);
        }
    }

    public void getBitcoinAddressByMasterXPublicKey(long index, Wallet wallet, boolean isInternal) {
        String publicKey = isInternal ? wallet.getInternalPublicKey() : wallet.getExtentedPublicKey();
        BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(publicKey, index, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().refreshAddressFail(isInternal);
                    return;
                }
                JsResult result = null;
                try {
                    result = JsonUtils.deserialize(jsResult, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    getMvpView().refreshAddressFail(isInternal);
                    return;
                }
                if (result.status == JsResult.STATUS_SUCCESS) {
                    String[] data = result.getData();
                    if (StringUtils.isEmpty(data)) {
                        getMvpView().refreshAddressFail(isInternal);
                        return;
                    }
                    String address = data[0];

                    if (StringUtils.isEmpty(address)) {
                        getMvpView().refreshAddressFail(isInternal);
                        return;
                    }
                    if (isInternal) {
                        wallet.setLastChangeAddress(address);
                        wallet.setLastChangeAddressIndex(index);
                    } else {
                        wallet.setLastAddress(address);
                        wallet.setLastAddressIndex(index);
                    }
                    //更新地址index
                    updateAddressIndex(wallet, data, false, isInternal);
                } else {
                    getMvpView().refreshAddressFail(isInternal);
                }

            }
        });
    }

    public void getBitcoinContinuousAddress(long fromIndex, long toIndex, Wallet wallet, boolean isInternal) {
        String publicKey = isInternal ? wallet.getInternalPublicKey() : wallet.getExtentedPublicKey();
        BitcoinJsWrapper.getInstance().getBitcoinContinuousAddressByMasterXPublicKey(publicKey, fromIndex, toIndex, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String jsResult) {

                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().refreshAddressFail(isInternal);
                    return;
                }
                JsResult result = null;
                try {
                    result = JsonUtils.deserialize(jsResult, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    getMvpView().refreshAddressFail(isInternal);
                    return;
                }
                if (result.status == JsResult.STATUS_SUCCESS) {
                    String[] data = result.getData();
                    if (data == null || toIndex - fromIndex + 1 != data.length) {
                        getMvpView().refreshAddressFail(isInternal);
                        return;
                    }
                    String address = data[data.length - 1];

                    if (StringUtils.isEmpty(address)) {
                        getMvpView().refreshAddressFail(isInternal);
                        return;
                    }

                    if (isInternal) {
                        wallet.setLastChangeAddress(address);
                        wallet.setLastChangeAddressIndex(toIndex);
                    } else {
                        wallet.setLastAddress(address);
                        wallet.setLastAddressIndex(toIndex);
                    }
                    //更新地址index
                    updateAddressIndex(wallet, data, false, isInternal);
                } else {
                    getMvpView().refreshAddressFail(isInternal);
                }

            }
        });
    }

    public void updateAddressIndex(Wallet wallet, String[] addressArray, boolean isLoad, boolean isInternal) {
        List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < addressArray.length; i++) {
            //构造address列表
            long index = wallet.getLastAddressIndex() - (addressArray.length - 1) + i;
            addressList.add(new Address(null, addressArray[i], wallet.getId(), index, AppConstants.BTC_COIN_TYPE, new Date(), 0l, isInternal));
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
                        if (isLoad) {
                            if (aBoolean) {
                                getMvpView().loadAddressSuccess(wallet.getLastAddress());
                            } else {
                                getMvpView().loadAddressFail();
                            }
                        } else {
                            if (aBoolean) {
                                getMvpView().refreshAddressSuccess(isInternal ? wallet.getLastChangeAddress() : wallet.getLastAddress(), isInternal);
                            } else {
                                getMvpView().refreshAddressFail(isInternal);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (isLoad) {
                            getMvpView().loadAddressFail();
                        } else {
                            getMvpView().refreshAddressFail(isInternal);
                        }
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

    public boolean isValidPublicKey() {
        if (StringUtils.isEmpty(getMvpView().getWallet().getExtentedPublicKey()) || StringUtils.isEmpty(getMvpView().getWallet().getInternalPublicKey())) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }


}
