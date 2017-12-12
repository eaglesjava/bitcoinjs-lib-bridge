package com.bitbill.www.ui.wallet.init;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu@163.com on 2017/12/12.
 */
public interface ResetPwdMvpView extends MvpView {
    String getOldPwd();

    String getNewPwd();

    String getConfirmPwd();

    Wallet getWallet();

    void oldPwdError();

    void resetPwdFail();

    void resetPwdSuccess();
}
