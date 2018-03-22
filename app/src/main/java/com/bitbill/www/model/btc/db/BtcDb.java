package com.bitbill.www.model.btc.db;

import com.bitbill.www.common.base.model.db.Db;
import com.bitbill.www.model.btc.db.entity.Address;
import com.bitbill.www.model.btc.db.entity.TxRecord;
import com.bitbill.www.model.btc.network.entity.TxElement;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by isanwenyu on 2018/1/10.
 */

public interface BtcDb extends Db {

    Long insertTxRecordAndInputsOutputs(TxRecord txRecord, List<TxElement.InputsBean> inputs, List<TxElement.OutputsBean> outputs);

    Observable<List<TxRecord>> getTxRecords();

    Observable<List<TxRecord>> getUnConfirmedTxRecord();

    Observable<Long> insertAddress(final Address address);

    Observable<Boolean> insertAddressList(final List<Address> addressList);

    Observable<Boolean> insertAddressListAndUpdatWallet(final List<Address> addressList, Wallet wallet);

    Observable<Boolean> deleteAddress(final Address address);

    Observable<Boolean> updateAddress(final Address address);

    Observable<Boolean> updateAddressList(final List<Address> addressList);

    Observable<List<Address>> getAllAddressList();

    Observable<List<Address>> getAddressById(Long addressId);

    Observable<List<Address>> getAddressByWalletId(Long walletId);

    Observable<List<Address>> getExtenalAddressLimitByWalletId(Long walletId, int limit);

    Address getAddressByName(String address);

    Address getAddressByNameAndWalletId(String address, Long walletId);

    List<Wallet> getWalletsByAddresses(List<String> addressList);
}
