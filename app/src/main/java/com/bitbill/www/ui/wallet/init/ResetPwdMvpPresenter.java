package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.wallet.WalletModel;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public interface ResetPwdMvpPresenter<M extends WalletModel, V extends ResetPwdMvpView> extends MvpPresenter<V> {

    void checkOldPwd();

    void resetPwd();
}
