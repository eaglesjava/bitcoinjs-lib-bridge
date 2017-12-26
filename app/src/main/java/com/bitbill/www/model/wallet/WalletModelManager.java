/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.wallet;


import android.content.Context;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.wallet.db.WalletDb;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.GetConfigResponse;
import com.bitbill.www.model.wallet.network.WalletApi;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressRequest;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxElement;
import com.bitbill.www.model.wallet.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxHistoryRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressResponse;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.SendTransactionRequest;
import com.bitbill.www.model.wallet.network.entity.SendTransactionResponse;
import com.bitbill.www.model.wallet.network.entity.TxHistory;
import com.bitbill.www.model.wallet.network.entity.Unconfirm;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */

@Singleton
public class WalletModelManager extends ModelManager implements WalletModel {

    private final WalletDb mWalletDb;
    private final Context mContext;
    private final WalletApi mWalletApi;

    @Inject
    public WalletModelManager(@ApplicationContext Context context, WalletDb walletDb, WalletApi walletApi) {
        mContext = context;
        mWalletDb = walletDb;
        mWalletApi = walletApi;
    }


    @Override
    public Observable<Long> insertWallet(Wallet wallet) {
        return mWalletDb.insertWallet(wallet);
    }

    @Override
    public Observable<Boolean> updateWallet(Wallet wallet) {
        return mWalletDb.updateWallet(wallet);
    }

    @Override
    public Observable<List<Wallet>> getAllWallets() {
        return mWalletDb.getAllWallets();
    }

    @Override
    public Observable<Wallet> getWalletById(Long walletId) {
        return mWalletDb.getWalletById(walletId);
    }

    @Override
    public Observable<Wallet> getWalletByMnemonicHash(java.lang.String mnemonicHash) {
        return mWalletDb.getWalletByMnemonicHash(mnemonicHash);
    }

    @Override
    public Observable<Wallet> getWalletBySeedHash(java.lang.String seedHash) {
        return mWalletDb.getWalletBySeedHash(seedHash);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mWalletApi.getApiHeader();
    }

    /**
     * @param createWalletRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<java.lang.String>> createWallet(CreateWalletRequest createWalletRequest) {
        return mWalletApi.createWallet(createWalletRequest);
    }

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<java.lang.String>> importWallet(ImportWalletRequest importWalletRequest) {
        return mWalletApi.importWallet(importWalletRequest);
    }

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<java.lang.String>> checkWalletId(CheckWalletIdRequest checkWalletIdRequest) {
        return mWalletApi.checkWalletId(checkWalletIdRequest);
    }

    /**
     * 获取walletId
     *
     * @param getWalletIdRequest 扩展公钥MD5
     * @return
     */
    @Override
    public Observable<ApiResponse<GetWalletIdResponse>> getWalletId(GetWalletIdRequest getWalletIdRequest) {
        return mWalletApi.getWalletId(getWalletIdRequest);
    }

    /**
     * 查询余额
     *
     * @param getBalanceRequest
     * @return
     */
    @Override
    public Observable<ApiResponse> getBalance(GetBalanceRequest getBalanceRequest) {
        return mWalletApi.getBalance(getBalanceRequest);
    }

    /**
     * 扫描地址
     *
     * @param refreshAddressRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<RefreshAddressResponse>> refreshAddress(RefreshAddressRequest refreshAddressRequest) {
        return mWalletApi.refreshAddress(refreshAddressRequest);
    }

    @Override
    public Observable<ApiResponse<GetTxElementResponse>> getTxElement(GetTxElement getTxElement) {
        return mWalletApi.getTxElement(getTxElement);
    }

    @Override
    public Observable<ApiResponse<SendTransactionResponse>> sendTransaction(SendTransactionRequest sendTransactionRequest) {
        return mWalletApi.sendTransaction(sendTransactionRequest);
    }

    /**
     * 交易记录
     *
     * @param getTxHistoryRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<List<TxHistory>>> getTxHistory(GetTxHistoryRequest getTxHistoryRequest) {
        return mWalletApi.getTxHistory(getTxHistoryRequest);
    }

    /**
     * 未确认交易列表
     *
     * @param listUnconfirmRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<List<Unconfirm>>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest) {
        return mWalletApi.listUnconfirm(listUnconfirmRequest);
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<GetConfigResponse>> getConfig() {
        return mWalletApi.getConfig();
    }

    /**
     * 搜索WalletId
     *
     * @param searchWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<SearchWalletIdResponse>> searchWalletId(SearchWalletIdRequest searchWalletIdRequest) {
        return mWalletApi.searchWalletId(searchWalletIdRequest);
    }


    /**
     * 获取联系人最新地址
     *
     * @param getLastAddressRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<GetLastAddressResponse>> getLastAddress(GetLastAddressRequest getLastAddressRequest) {
        return mWalletApi.getLastAddress(getLastAddressRequest);
    }
}
