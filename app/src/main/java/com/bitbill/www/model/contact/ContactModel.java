/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.model.contact;

import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.model.contact.db.ContactDb;
import com.bitbill.www.model.contact.network.ContactApi;

/**
 * Created by isanwenyu@163.com on 2017/7/25.
 */
public interface ContactModel extends Model, ContactDb, ContactApi {

}
