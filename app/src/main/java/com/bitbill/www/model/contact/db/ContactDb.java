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

import com.bitbill.www.common.base.model.db.Db;
import com.bitbill.www.model.contact.db.entity.Contact;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by zhuyuanbao on 2017/07/17.
 */

public interface ContactDb extends Db {

    Observable<Long> insertContact(final Contact contact);

    Observable<Boolean> insertContacts(final List<Contact> contacts);

    Observable<Boolean> deleteContact(final Contact contact);

    Observable<Boolean> updateContact(final Contact contact);

    Observable<List<Contact>> getAllContacts();

    Observable<Contact> getContactById(Long contactId);

    Observable<Contact> getContactByWalletId(String walletId);

    Observable<Contact> getContactByAddress(String address);


}
