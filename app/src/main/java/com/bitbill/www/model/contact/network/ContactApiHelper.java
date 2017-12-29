package com.bitbill.www.model.contact.network;

import com.bitbill.www.common.base.model.network.api.ApiEndPoint;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiHelper;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.BaseUrlInfo;
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
import com.google.gson.reflect.TypeToken;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public class ContactApiHelper extends ApiHelper implements ContactApi {

    @Inject
    public ContactApiHelper(ApiHeader apiHeader, @BaseUrlInfo java.lang.String baseUrl) {
        super(apiHeader, baseUrl);
    }

    /**
     * 增加联系人
     *
     * @param addContactsRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<AddContactsResponse>> addContacts(AddContactsRequest addContactsRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ADD_CONTACTS)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(addContactsRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<AddContactsResponse>>() {
                });
    }

    /**
     * @param updateContactsRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<Void>> updateContacts(UpdateContactsRequest updateContactsRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.UPDATE_CONTACTS)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(updateContactsRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<Void>>() {
                });
    }

    @Override
    public Observable<ApiResponse<RecoverContactsResponse>> recoverContacts(RecoverContactsRequest recoverContactsRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.RECOVER_CONTACTS)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(recoverContactsRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<RecoverContactsResponse>>() {
                });
    }

    @Override
    public Observable<ApiResponse<Void>> deleteContacts(DeleteContactsRequest deleteContactsRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.DELETE_CONTACTS)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(deleteContactsRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<Void>>() {
                });
    }

    /**
     * 搜索WalletId
     *
     * @return
     */
    @Override
    public Observable<ApiResponse<SearchWalletIdResponse>> searchWalletId(SearchWalletIdRequest searchWalletIdRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.SEARCH_WALLETID)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(searchWalletIdRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<SearchWalletIdResponse>>() {
                });
    }

    /**
     * 获取联系人最新地址
     *
     * @param getLastAddressRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<GetLastAddressResponse>> getLastAddress(GetLastAddressRequest getLastAddressRequest) {
        return Rx2AndroidNetworking.post(ApiEndPoint.GET_LAST_ADDRESS)
                .addHeaders(mApiHeader.getPublicApiHeader())
                .addApplicationJsonBody(getLastAddressRequest)
                .build()
                .getParseObservable(new TypeToken<ApiResponse<GetLastAddressResponse>>() {
                });
    }

}
