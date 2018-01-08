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
import com.bitbill.www.model.wallet.network.WalletApi;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.DeleteWalletRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetCacheVersionRequest;
import com.bitbill.www.model.wallet.network.entity.GetConfigResponse;
import com.bitbill.www.model.wallet.network.entity.GetExchangeRateResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxElement;
import com.bitbill.www.model.wallet.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.wallet.network.entity.GetTxInfoResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxListRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletResponse;
import com.bitbill.www.model.wallet.network.entity.ListTxElementResponse;
import com.bitbill.www.model.wallet.network.entity.ListUnconfirmRequest;
import com.bitbill.www.model.wallet.network.entity.SendTransactionRequest;
import com.bitbill.www.model.wallet.network.entity.SendTransactionResponse;

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
    public Observable<Boolean> insertWallets(List<Wallet> wallets) {
        return mWalletDb.insertWallets(wallets);
    }

    @Override
    public Observable<Boolean> updateWallet(Wallet wallet) {
        return mWalletDb.updateWallet(wallet);
    }

    @Override
    public Observable<Boolean> updateWallets(List<Wallet> walletList) {
        return mWalletDb.updateWallets(walletList);
    }

    @Override
    public Observable<Boolean> deleteWallet(Wallet wallet) {
        return mWalletDb.deleteWallet(wallet);
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
    public Observable<ApiResponse> createWallet(CreateWalletRequest createWalletRequest) {
        return mWalletApi.createWallet(createWalletRequest);
    }

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<ImportWalletResponse>> importWallet(ImportWalletRequest importWalletRequest) {
        return mWalletApi.importWallet(importWalletRequest);
    }

    @Override
    public Observable<ApiResponse> deleteWallet(DeleteWalletRequest deleteWalletRequest) {
        return mWalletApi.deleteWallet(deleteWalletRequest);
    }

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse> checkWalletId(CheckWalletIdRequest checkWalletIdRequest) {
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
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> getTxList(GetTxListRequest getTxListRequest) {
        return mWalletApi.getTxList(getTxListRequest);
    }

    /**
     * 未确认交易列表
     *
     * @param listUnconfirmRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<ListTxElementResponse>> listUnconfirm(ListUnconfirmRequest listUnconfirmRequest) {
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

    @Override
    public Observable<ApiResponse<GetTxInfoResponse>> getTxInfo(GetTxInfoRequest getTxInfoRequest) {
        return mWalletApi.getTxInfo(getTxInfoRequest);
    }

    @Override
    public Observable<ApiResponse<GetExchangeRateResponse>> getExchangeRate() {
        return mWalletApi.getExchangeRate();
    }

    @Override
    public Observable<ApiResponse> getCacheVersion(GetCacheVersionRequest getCacheVersionRequest) {
        return mWalletApi.getCacheVersion(getCacheVersionRequest);
    }

}
