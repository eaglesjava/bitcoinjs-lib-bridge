package com.bitbill.www.model.contact;

import android.content.Context;

import com.bitbill.www.common.base.model.ModelManager;
import com.bitbill.www.common.base.model.network.api.ApiHeader;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.di.qualifier.ApplicationContext;
import com.bitbill.www.model.contact.db.ContactDb;
import com.bitbill.www.model.contact.db.entity.Contact;
import com.bitbill.www.model.contact.network.ContactApi;
import com.bitbill.www.model.contact.network.entity.AddContactsRequest;
import com.bitbill.www.model.contact.network.entity.AddContactsResponse;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressRequest;
import com.bitbill.www.model.wallet.network.entity.GetLastAddressResponse;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.SearchWalletIdResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public class ContactModelManager extends ModelManager implements ContactModel {

    private final ContactDb mContactDb;
    private final Context mContext;
    private final ContactApi mContactApi;

    @Inject
    public ContactModelManager(@ApplicationContext Context context, ContactDb contactDb, ContactApi contactApi) {
        mContext = context;
        mContactDb = contactDb;
        mContactApi = contactApi;
    }

    /**
     * 增加联系人
     *
     * @param addContactsRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<AddContactsResponse>> addContacts(AddContactsRequest addContactsRequest) {
        return mContactApi.addContacts(addContactsRequest);
    }

    @Override
    public ApiHeader getApiHeader() {
        return mContactApi.getApiHeader();
    }

    @Override
    public Observable<Long> insertContact(Contact contact) {
        return mContactDb.insertContact(contact);
    }

    @Override
    public Observable<Boolean> updateContact(Contact contact) {
        return mContactDb.updateContact(contact);
    }

    @Override
    public Observable<List<Contact>> getAllContacts() {
        return mContactDb.getAllContacts();
    }

    @Override
    public Observable<Contact> getContactById(Long contactId) {
        return mContactDb.getContactById(contactId);
    }

    /**
     * 搜索WalletId
     *
     * @param searchWalletIdRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<SearchWalletIdResponse>> searchWalletId(SearchWalletIdRequest searchWalletIdRequest) {
        return mContactApi.searchWalletId(searchWalletIdRequest);
    }


    /**
     * 获取联系人最新地址
     *
     * @param getLastAddressRequest
     * @return
     */
    @Override
    public Observable<ApiResponse<GetLastAddressResponse>> getLastAddress(GetLastAddressRequest getLastAddressRequest) {
        return mContactApi.getLastAddress(getLastAddressRequest);
    }
}
