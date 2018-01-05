package com.bitbill.www.model.address;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.model.address.db.AddressDb;
import com.bitbill.www.model.address.db.entity.Address;

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

    @Inject
    public AddressModelManager(AddressDb addressDb) {
        mAddressDb = addressDb;
    }

    @Override
    public Observable<Long> insertAddress(Address address) {
        return mAddressDb.insertAddress(address);
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
}
