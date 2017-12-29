package com.bitbill.www.model.contact.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.contact.network.entity.AddContactsRequest;
import com.bitbill.www.model.contact.network.entity.AddContactsResponse;
import com.bitbill.www.model.contact.network.entity.DeleteContactsRequest;
import com.bitbill.www.model.contact.network.entity.RecoverContactsRequest;
import com.bitbill.www.model.contact.network.entity.RecoverContactsResponse;
import com.bitbill.www.model.contact.network.entity.UpdateContactsRequest;
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
     * 修改联系人
     *
     * @param updateContactsRequest
     * @return
     */
    Observable<ApiResponse<Void>> updateContacts(UpdateContactsRequest updateContactsRequest);

    /**
     * 恢复联系人
     *
     * @param recoverContactsRequest
     * @return
     */
    Observable<ApiResponse<RecoverContactsResponse>> recoverContacts(RecoverContactsRequest recoverContactsRequest);

    /**
     * 删除联系人
     *
     * @param deleteContactsRequest
     * @return
     */
    Observable<ApiResponse<Void>> deleteContacts(DeleteContactsRequest deleteContactsRequest);

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