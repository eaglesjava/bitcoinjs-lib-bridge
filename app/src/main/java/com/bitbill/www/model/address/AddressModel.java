package com.bitbill.www.model.address;

import com.bitbill.www.common.base.model.Model;
import com.bitbill.www.model.address.db.AddressDb;
import com.bitbill.www.model.address.network.AddressApi;

/**
 * Created by isanwenyu on 2018/1/5.
 */

public interface AddressModel extends Model, AddressDb, AddressApi {
}
