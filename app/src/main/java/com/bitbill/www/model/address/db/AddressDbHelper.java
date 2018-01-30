package com.bitbill.www.model.address.db;

import com.bitbill.model.db.dao.AddressDao;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.model.db.dao.WalletDao;
import com.bitbill.www.common.base.model.db.DbHelper;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/5.
 */

@Singleton
public class AddressDbHelper extends DbHelper implements AddressDb {

    private final AddressDao mAddressDao;
    private final WalletDao mWalletDao;

    @Inject
    public AddressDbHelper(@DatabaseInfo DaoSession daoSession) {
        super(daoSession);
        mAddressDao = daoSession.getAddressDao();
        mWalletDao = daoSession.getWalletDao();
    }

    @Override
    public Observable<Long> insertAddress(Address address) {
        return Observable.fromCallable(() -> mAddressDao.insert(address));
    }

    @Override
    public Observable<Boolean> insertAddressList(List<Address> addressList) {
        return Observable.fromCallable(() -> {
            mAddressDao.insertInTx(addressList);
            return true;
        });
    }

    @Override
    public Observable<Boolean> insertAddressListAndUpdatWallet(List<Address> addressList, Wallet wallet) {
        return Observable.fromCallable(() -> mDaoSession.callInTxNoException(() -> {
            mAddressDao.insertOrReplaceInTx(addressList);
            mWalletDao.update(wallet);
            return true;
        }));
    }

    @Override
    public Observable<Boolean> deleteAddress(Address address) {
        return Observable.fromCallable(() -> {
            mAddressDao.delete(address);
            return true;
        });
    }

    @Override
    public Observable<Boolean> updateAddress(Address address) {
        return Observable.fromCallable(() -> {
            mAddressDao.update(address);
            return true;
        });
    }

    @Override
    public Observable<Boolean> updateAddressList(List<Address> addressList) {
        return Observable.fromCallable(() -> {
            mAddressDao.updateInTx(addressList);
            return true;
        });
    }

    @Override
    public Observable<List<Address>> getAllAddressList() {
        return Observable.fromCallable(() -> mAddressDao.loadAll());
    }

    @Override
    public Observable<List<Address>> getAddressById(Long addressId) {
        return Observable.fromCallable(() -> mAddressDao.queryBuilder().where(AddressDao.Properties.Id.eq(addressId)).list());
    }

    @Override
    public Observable<List<Address>> getAddressByWalletId(Long walletId) {
        return Observable.fromCallable(() -> mAddressDao.queryBuilder().where(AddressDao.Properties.WalletId.eq(walletId)).list());

    }

    @Override
    public Address getAddressByName(String address) {
        return mAddressDao.queryBuilder().where(AddressDao.Properties.Name.eq(address)).unique();
    }

    @Override
    public Address getAddressByNameAndWalletId(String address, Long walletId) {
        return mAddressDao.queryBuilder().where(AddressDao.Properties.Name.eq(address), AddressDao.Properties.WalletId.eq(walletId)).unique();
    }

    @Override
    public List<Wallet> getWalletsByAddresses(List<String> addressList) {
        List<Address> addresses = mAddressDao.queryBuilder().where(AddressDao.Properties.Name.in(addressList)).list();
        List<Wallet> wallets = new ArrayList<>();
        if (StringUtils.isEmpty(addresses)) {
            for (Address address : addresses) {
                wallets.add(address.getWallet());
            }
        }
        return wallets;
    }
}
