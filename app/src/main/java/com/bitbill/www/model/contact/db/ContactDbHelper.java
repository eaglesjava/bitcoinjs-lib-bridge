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

package com.bitbill.www.model.contact.db;

import com.bitbill.www.common.base.model.db.DbHelper;
import com.bitbill.www.di.qualifier.DatabaseInfo;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.contact.db.entity.ContactDao;
import com.bitbill.www.model.contact.db.entity.DaoSession;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


/**
 * Created by zhuyuanbao on 2017/07/17.
 */

@Singleton
public class ContactDbHelper extends DbHelper implements ContactDb {

    private final ContactDao mContactDao;

    @Inject
    public ContactDbHelper(@DatabaseInfo DaoSession daoSession) {
        super(daoSession);
        mContactDao = mDaoSession.getContactDao();
    }

    @Override
    public Observable<Long> insertContact(final Contact contact) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return mContactDao.insert(contact);
            }
        });
    }

    @Override
    public Observable<Boolean> updateContact(Contact wallet) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mContactDao.update(wallet);
                return true;
            }
        });
    }

    @Override
    public Observable<List<Contact>> getAllContacts() {
        return Observable.fromCallable(new Callable<List<Contact>>() {
            @Override
            public List<Contact> call() throws Exception {
                return mContactDao.loadAll();
            }
        });
    }

    @Override
    public Observable<Contact> getContactById(Long contactId) {
        return Observable.fromCallable(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return mContactDao.load(contactId);
            }
        });
    }


}
