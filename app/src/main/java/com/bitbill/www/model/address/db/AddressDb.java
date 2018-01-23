package com.bitbill.www.model.address.db;

import com.bitbill.www.common.base.model.db.Db;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/5.
 */

public interface AddressDb extends Db {

    Observable<Long> insertAddress(final Address address);

    Observable<Boolean> insertAddressList(final List<Address> addressList);

    Observable<Boolean> insertAddressListAndUpdatWallet(final List<Address> addressList, Wallet wallet);

    Observable<Boolean> deleteAddress(final Address address);

    Observable<Boolean> updateAddress(final Address address);

    Observable<Boolean> updateAddressList(final List<Address> addressList);

    Observable<List<Address>> getAllAddressList();

    Observable<List<Address>> getAddressById(Long addressId);

    Observable<List<Address>> getAddressByWalletId(Long walletId);

    Address getAddressByName(String address);

    List<Wallet> getWalletsByAddresses(List<String> addressList);

}
