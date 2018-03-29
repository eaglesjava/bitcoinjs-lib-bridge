package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu on 2018/1/31.
 */

public interface UpdateMvpPresenter<M extends AppModel, V extends UpdateMvpView> extends MvpPresenter<V> {

    void getConfig();

    void checkUpdate();

    void setUpdateCancelTime();
}
