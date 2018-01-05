package com.bitbill.www.model.address.db;

import com.bitbill.www.common.base.model.db.Db;
import com.bitbill.www.model.address.db.entity.Address;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/5.
 */

public interface AddressDb extends Db {

    Observable<Long> insertAddress(final Address address);

    Observable<Boolean> deleteAddress(final Address address);

    Observable<Boolean> updateAddress(final Address address);

    Observable<List<Address>> getAllAddressList();

    Observable<List<Address>> getAddressById(Long addressId);

    Observable<List<Address>> getAddressByWalletId(Long walletId);

}
