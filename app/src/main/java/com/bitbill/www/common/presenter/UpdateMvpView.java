package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu on 2018/1/31.
 */

public interface UpdateMvpView extends MvpView {
    void needUpdateApp(boolean needUpdate, boolean needForce, String updateVersion);

    void getConfigSuccess(String aversion, String aforceVersion);

    void getConfigFail();
}
