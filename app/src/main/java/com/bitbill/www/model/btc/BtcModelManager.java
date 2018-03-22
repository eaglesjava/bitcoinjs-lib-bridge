package com.bitbill.www.model.btc;

import android.content.Context;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.btc.db.BtcDb;
import com.bitbill.www.model.btc.db.entity.Address;
import com.bitbill.www.model.btc.db.entity.TxRecord;
import com.bitbill.www.model.btc.network.BtcApi;
import com.bitbill.www.model.btc.network.entity.GetTxElement;
import com.bitbill.www.model.btc.network.entity.GetTxElementResponse;
import com.bitbill.www.model.btc.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.btc.network.entity.GetTxListRequest;
import com.bitbill.www.model.btc.network.entity.ListTxElementResponse;
import com.bitbill.www.model.btc.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.btc.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.btc.network.entity.RefreshAddressResponse;
import com.bitbill.www.model.btc.network.entity.SendTransactionRequest;
import com.bitbill.www.model.btc.network.entity.SendTransactionResponse;
import com.bitbill.www.model.btc.network.entity.TxElement;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2018/1/10.
 */

public class BtcModelManager extends ModelManager implements BtcModel {

    private final BtcDb mBtcDb;
    private final Context mContext;
    private final BtcApi mBtcApi;

    @Inject
    public BtcModelManager(@ApplicationContext Context context, BtcDb btcDb, BtcApi btcApi) {
        mContext = context;
        mBtcDb = btcDb;
        mBtcApi = btcApi;
    }

    @Override
    public Observable<ApiResponse<GetTxElementResponse>> getTxElement(GetTxElement getTxElement) {
        return mBtcApi.getTxElement(getTxElement);
    }

    @Override
    public Observable<ApiResponse<SendTransactionResponse>> sendTransaction(SendTransactionRequest sendTransactionRequest) {
        return mBtcApi.sendTransaction(sendTransactionRequest);
    }

    /**
     * 交易记录
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> getTxList(GetTxListRequest getTxListRequest) {
        return mBtcApi.getTxList(getTxListRequest);
    }

    /**
     * 未确认交易列表
     *
     * @param listUnconfirmRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest) {
        return mBtcApi.listUnconfirm(listUnconfirmRequest);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mBtcApi.getApiHeader();
    }

    @Override
    public Long insertTxRecordAndInputsOutputs(TxRecord txRecord, List<TxElement.InputsBean> inputs, List<TxElement.OutputsBean> outputs) {
        return mBtcDb.insertTxRecordAndInputsOutputs(txRecord, inputs, outputs);
    }

    @Override
    public Observable<List<TxRecord>> getTxRecords() {
        return mBtcDb.getTxRecords();
    }

    @Override
    public Observable<List<TxRecord>> getUnConfirmedTxRecord() {
        return mBtcDb.getUnConfirmedTxRecord();
    }

    @Override
    public Observable<Long> insertAddress(Address address) {
        return mBtcDb.insertAddress(address);
    }

    @Override
    public Observable<Boolean> insertAddressList(List<Address> addressList) {
        return mBtcDb.insertAddressList(addressList);
    }

    @Override
    public Observable<Boolean> insertAddressListAndUpdatWallet(List<Address> addressList, Wallet wallet) {
        return mBtcDb.insertAddressListAndUpdatWallet(addressList, wallet);
    }

    @Override
    public Observable<Boolean> deleteAddress(Address address) {
        return mBtcDb.deleteAddress(address);
    }

    @Override
    public Observable<Boolean> updateAddress(Address address) {
        return mBtcDb.updateAddress(address);
    }

    @Override
    public Observable<Boolean> updateAddressList(List<Address> addressList) {
        return mBtcDb.updateAddressList(addressList);
    }

    @Override
    public Observable<List<Address>> getAllAddressList() {
        return mBtcDb.getAllAddressList();
    }

    @Override
    public Observable<List<Address>> getAddressById(Long addressId) {
        return mBtcDb.getAddressById(addressId);
    }

    @Override
    public Observable<List<Address>> getAddressByWalletId(Long walletId) {
        return mBtcDb.getAddressByWalletId(walletId);
    }

    @Override
    public Observable<List<Address>> getExtenalAddressLimitByWalletId(Long walletId, int limit) {
        return mBtcDb.getExtenalAddressLimitByWalletId(walletId, limit);
    }

    @Override
    public Address getAddressByName(String address) {
        return mBtcDb.getAddressByName(address);
    }

    @Override
    public Address getAddressByNameAndWalletId(String address, Long walletId) {
        return mBtcDb.getAddressByNameAndWalletId(address, walletId);
    }

    @Override
    public List<Wallet> getWalletsByAddresses(List<String> addressList) {
        return mBtcDb.getWalletsByAddresses(addressList);
    }

    @Override
    public Observable<ApiResponse<TxElement>> getTxInfo(GetTxInfoRequest getTxInfoRequest) {
        return mBtcApi.getTxInfo(getTxInfoRequest);
    }

    @Override
    public Observable<ApiResponse<RefreshAddressResponse>> refreshAddress(RefreshAddressRequest refreshAddressRequest) {
        return mBtcApi.refreshAddress(refreshAddressRequest);
    }

}
