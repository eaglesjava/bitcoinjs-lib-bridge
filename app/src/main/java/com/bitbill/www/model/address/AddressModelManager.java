package com.bitbill.www.model.address;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.address.db.AddressDb;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.address.network.AddressApi;
import com.bitbill.www.model.address.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.address.network.entity.RefreshAddressResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/5.
 */

@Singleton
public class AddressModelManager extends ModelManager implements AddressModel {

    private final AddressDb mAddressDb;
    private final AddressApi mAddressApi;

    @Inject
    public AddressModelManager(AddressDb addressDb, AddressApi addressApi) {
        mAddressDb = addressDb;
        mAddressApi = addressApi;
    }

    @Override
    public Observable<Long> insertAddress(Address address) {
        return mAddressDb.insertAddress(address);
    }

    @Override
    public Observable<Boolean> insertAddressList(List<Address> addressList) {
        return mAddressDb.insertAddressList(addressList);
    }

    @Override
    public Observable<Boolean> insertAddressListAndUpdatWallet(List<Address> addressList, Wallet wallet) {
        return mAddressDb.insertAddressListAndUpdatWallet(addressList, wallet);
    }

    @Override
    public Observable<Boolean> deleteAddress(Address address) {
        return mAddressDb.deleteAddress(address);
    }

    @Override
    public Observable<Boolean> updateAddress(Address address) {
        return mAddressDb.updateAddress(address);
    }

    @Override
    public Observable<Boolean> updateAddressList(List<Address> addressList) {
        return mAddressDb.updateAddressList(addressList);
    }

    @Override
    public Observable<List<Address>> getAllAddressList() {
        return mAddressDb.getAllAddressList();
    }

    @Override
    public Observable<List<Address>> getAddressById(Long addressId) {
        return mAddressDb.getAddressById(addressId);
    }

    @Override
    public Observable<List<Address>> getAddressByWalletId(Long walletId) {
        return mAddressDb.getAddressByWalletId(walletId);
    }

    @Override
    public Observable<List<Address>> getExtenalAddressLimitByWalletId(Long walletId, int limit) {
        return mAddressDb.getExtenalAddressLimitByWalletId(walletId, limit);
    }

    @Override
    public Address getAddressByName(String address) {
        return mAddressDb.getAddressByName(address);
    }

    @Override
    public Address getAddressByNameAndWalletId(String address, Long walletId) {
        return mAddressDb.getAddressByNameAndWalletId(address, walletId);
    }

    @Override
    public List<Wallet> getWalletsByAddresses(List<String> addressList) {
        return mAddressDb.getWalletsByAddresses(addressList);
    }

    @Override
    public Observable<Boolean> checkAddressIsUsed(String address) {
        return mAddressDb.checkAddressIsUsed(address);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mAddressApi.getApiHeader();
    }

    @Override
    public Observable<ApiResponse<RefreshAddressResponse>> refreshAddress(RefreshAddressRequest refreshAddressRequest) {
        return mAddressApi.refreshAddress(refreshAddressRequest);
    }
}
