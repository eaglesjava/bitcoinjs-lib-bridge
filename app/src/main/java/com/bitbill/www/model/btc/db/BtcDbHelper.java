package com.bitbill.www.model.btc.db;

import com.bitbill.model.db.dao.AddressDao;
import com.bitbill.model.db.dao.DaoSession;
import com.bitbill.model.db.dao.InputDao;
import com.bitbill.model.db.dao.OutputDao;
import com.bitbill.model.db.dao.TxRecordDao;
import com.bitbill.model.db.dao.WalletDao;
import com.bitbill.www.common.base.model.db.DbHelper;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.btc.db.entity.Address;
import com.bitbill.www.model.btc.db.entity.Input;
import com.bitbill.www.model.btc.db.entity.Output;
import com.bitbill.www.model.btc.db.entity.TxRecord;
import com.bitbill.www.model.btc.network.entity.TxElement;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/10.
 */

@Singleton
public class BtcDbHelper extends DbHelper implements BtcDb {

    private final TxRecordDao mTxRecordDao;
    private final InputDao mInputDao;
    private final OutputDao mOutputDao;
    private final AddressDao mAddressDao;
    private final WalletDao mWalletDao;

    @Inject
    public BtcDbHelper(@DatabaseInfo DaoSession daoSession) {
        super(daoSession);
        mTxRecordDao = daoSession.getTxRecordDao();
        mInputDao = daoSession.getInputDao();
        mOutputDao = daoSession.getOutputDao();
        mAddressDao = daoSession.getAddressDao();
        mWalletDao = daoSession.getWalletDao();
    }

    @Override
    public Long insertTxRecordAndInputsOutputs(TxRecord txRecord, List<TxElement.InputsBean> inputs, List<TxElement.OutputsBean> outputs) {

        return getDaoSession().callInTxNoException(() -> {
            long rowId = mTxRecordDao.insertOrReplace(txRecord);
            mOutputDao.insertOrReplaceInTx(getOutputs(outputs, txRecord));
            mInputDao.insertOrReplaceInTx(getInputs(inputs, txRecord));
            return rowId;
        });
    }

    @Override
    public Observable<List<TxRecord>> getTxRecords() {
        return Observable.fromCallable(() -> mTxRecordDao.loadAll());
    }

    @Override
    public Observable<List<TxRecord>> getUnConfirmedTxRecord() {
        return Observable.fromCallable(() -> mTxRecordDao.queryBuilder().where(TxRecordDao.Properties.Height.eq(-1)).orderDesc(TxRecordDao.Properties.CreatedTime).list());
    }

    private List<Output> getOutputs(List<TxElement.OutputsBean> outputs, TxRecord txRecord) {
        List<Output> oOutputs = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            TxElement.OutputsBean outputsBean = outputs.get(i);
            Output output = new Output();
            output.setAddress(outputsBean.getAddress());
            output.setValue(outputsBean.getValue());
            output.setTxId(txRecord.getId());
            output.setWalletId(txRecord.getWalletId());
            output.setTxHash(txRecord.getTxHash());
            output.setTxIndex(i);
            oOutputs.add(output);
        }
        return oOutputs;
    }

    private List<Input> getInputs(List<TxElement.InputsBean> inputs, TxRecord txRecord) {
        List<Input> nInputs = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            TxElement.InputsBean inputsBean = inputs.get(i);
            Input input = new Input();
            input.setTxId(txRecord.getId());
            input.setWalletId(txRecord.getWalletId());
            input.setAddress(inputsBean.getAddress());
            input.setValue(inputsBean.getValue());
            input.setTxHash(txRecord.getTxHash());
            input.setTxIndex(i);
            nInputs.add(input);
        }
        return nInputs;
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
    public Observable<List<Address>> getExtenalAddressLimitByWalletId(Long walletId, int limit) {
        return Observable.fromCallable(() -> mAddressDao.queryBuilder().where(AddressDao.Properties.WalletId.eq(walletId), AddressDao.Properties.IsInternal.eq(false)).orderDesc(AddressDao.Properties.Index).limit(limit).list());
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
