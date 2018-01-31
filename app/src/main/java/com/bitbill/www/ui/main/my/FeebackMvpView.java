package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu@163.com on 2018/1/31.
 */
public interface FeebackMvpView extends MvpView {

    String getContent();

    String getContact();

    void sendFeebackSuccess();

    void sendFeebackFail();

    void requireContent();

    void tooMuchWords();

    void requireContact();
}
