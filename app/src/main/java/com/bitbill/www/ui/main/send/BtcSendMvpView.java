package com.bitbill.www.ui.main.send;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2017/12/20.
 */

public interface BtcSendMvpView extends MvpView {

    String getSendAddress();

    void validateAddress(boolean validate);

    void requireAddress();
}
