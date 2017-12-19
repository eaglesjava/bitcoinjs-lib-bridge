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
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceResponse;
import com.bitbill.www.model.wallet.network.entity.GetTxElement;
import com.bitbill.www.model.wallet.network.entity.GetTxElementResponse;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressRequest;
import com.bitbill.www.model.wallet.network.entity.RefreshAddressResponse;
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
    public Observable<Wallet> getWalletByMnemonicHash(String mnemonicHash) {
        return mWalletDb.getWalletByMnemonicHash(mnemonicHash);
    }

    @Override
    public Observable<Wallet> getWalletBySeedHash(String seedHash) {
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
    public Observable<ApiResponse<String>> createWallet(CreateWalletRequest createWalletRequest) {
        return mWalletApi.createWallet(createWalletRequest);
    }

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<String>> importWallet(ImportWalletRequest importWalletRequest) {
        return mWalletApi.importWallet(importWalletRequest);
    }

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<String>> checkWalletId(CheckWalletIdRequest checkWalletIdRequest) {
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
    public Observable<ApiResponse<GetBalanceResponse>> getBalance(GetBalanceRequest getBalanceRequest) {
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
}
