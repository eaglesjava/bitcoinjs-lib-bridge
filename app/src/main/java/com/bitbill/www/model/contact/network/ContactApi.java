package com.bitbill.www.model.contact.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.contact.network.entity.AddContactsRequest;
import com.bitbill.www.model.contact.network.entity.AddContactsResponse;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressRequest;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressResponse;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdResponse;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface ContactApi extends Api {

    /**
     * 增加联系人
     *
     * @param addContactsRequest
     * @return
     */
    Observable<ApiResponse<AddContactsResponse>> addContacts(AddContactsRequest addContactsRequest);

    /**
     * 搜索WalletId
     *
     * @return
     */
    Observable<ApiResponse<SearchWalletIdResponse>> searchWalletId(SearchWalletIdRequest searchWalletIdRequest);

    /**
     * 获取联系人最新地址
     *
     * @param getLastAddressRequest
     * @return
     */
    Observable<ApiResponse<GetLastAddressResponse>> getLastAddress(GetLastAddressRequest getLastAddressRequest);
}
