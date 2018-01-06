package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface GetCacheVersionMvpView extends MvpView {

    void getResponseAddressIndex(long indexNo, long lastIndex, Wallet wallet);
}
