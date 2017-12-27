package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2017/12/26.
 */

public interface GetLastAddressMvpView extends MvpView {
    String getWalletId();

    void getLastAddressSuccess(String address);

    void getLastAddressFail();

    void requireWalletId();
}
