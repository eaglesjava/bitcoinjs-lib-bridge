package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2018/1/13.
 */

public interface SyncAddressMvpView extends MvpView {
    void syncAddressSuccess(Wallet wallet, boolean isInternal);
}
