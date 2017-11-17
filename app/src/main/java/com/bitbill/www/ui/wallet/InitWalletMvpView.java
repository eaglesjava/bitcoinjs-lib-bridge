package com.bitbill.www.ui.wallet;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
public interface InitWalletMvpView extends MvpView {

    void initWalletSuccess();

    void initWalletFail();

    void createMnemonicSuccess();

    void createMnemonicFail();
}
