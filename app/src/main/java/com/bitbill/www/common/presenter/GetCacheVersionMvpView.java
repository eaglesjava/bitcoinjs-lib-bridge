package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */
public interface GetCacheVersionMvpView extends MvpView {

    void getResponseAddressIndex(long indexNo, Wallet wallet);

    void getDiffVersionWallets(List<Wallet> tmpWalletList);
}
