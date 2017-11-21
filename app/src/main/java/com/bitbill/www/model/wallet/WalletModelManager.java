/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.wallet;


import android.content.Context;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.wallet.db.WalletDb;
import com.bitbill.www.model.wallet.db.entity.Wallet;

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

    @Inject
    public WalletModelManager(@ApplicationContext Context context, WalletDb walletDb) {
        mContext = context;
        mWalletDb = walletDb;
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
}
