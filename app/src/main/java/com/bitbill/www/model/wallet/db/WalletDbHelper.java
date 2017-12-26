/*
 * Copyright (c) 2017 askcoin.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.bitbill.www.model.wallet.db;

import com.bitbill.www.common.base.model.db.DbHelper;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.contact.db.entity.DaoSession;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.db.entity.WalletDao;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


/**
 * Created by zhuyuanbao on 2017/07/17.
 */

@Singleton
public class WalletDbHelper extends DbHelper implements WalletDb {

    private final WalletDao mWalletDao;

    @Inject
    public WalletDbHelper(@DatabaseInfo DaoSession daoSession) {
        super(daoSession);
        mWalletDao = mDaoSession.getWalletDao();
    }

    @Override
    public Observable<Long> insertWallet(final Wallet wallet) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mWalletDao.insert(wallet);
            }
        });
    }

    @Override
    public Observable<Boolean> updateWallet(Wallet wallet) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mWalletDao.update(wallet);
                return true;
            }
        });
    }

    @Override
    public Observable<List<Wallet>> getAllWallets() {
        return Observable.fromCallable(new Callable<List<Wallet>>() {
            @Override
            public List<Wallet> call() throws Exception {
                return mWalletDao.loadAll();
            }
        });
    }

    @Override
    public Observable<Wallet> getWalletById(Long walletId) {
        return Observable.fromCallable(new Callable<Wallet>() {
            @Override
            public Wallet call() throws Exception {
                return mWalletDao.load(walletId);
            }
        });
    }

    @Override
    public Observable<Wallet> getWalletByMnemonicHash(String mnemonicHash) {
        return Observable.fromCallable(new Callable<Wallet>() {
            @Override
            public Wallet call() throws Exception {
                return mWalletDao.queryBuilder().where(WalletDao.Properties.MnemonicHash.eq(mnemonicHash)).unique();
            }
        });
    }

    @Override
    public Observable<Wallet> getWalletBySeedHash(String seedHash) {
        return Observable.fromCallable(new Callable<Wallet>() {
            @Override
            public Wallet call() throws Exception {
                return mWalletDao.queryBuilder().where(WalletDao.Properties.SeedHexHash.eq(seedHash)).unique();
            }
        });
    }

}
