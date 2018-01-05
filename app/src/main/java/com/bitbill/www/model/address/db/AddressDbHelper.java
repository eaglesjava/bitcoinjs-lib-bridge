package com.bitbill.www.model.address.db;

import com.bitbill.www.common.base.model.db.DbHelper;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.address.db.entity.Address;
import com.bitbill.www.model.address.db.entity.AddressDao;
import com.bitbill.www.model.contact.db.entity.DaoSession;

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

    @Inject
    public AddressDbHelper(@DatabaseInfo DaoSession daoSession) {
        super(daoSession);
        mAddressDao = daoSession.getAddressDao();
    }

    @Override
    public Observable<Long> insertAddress(Address address) {
        return Observable.fromCallable(() -> mAddressDao.insert(address));
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
}
