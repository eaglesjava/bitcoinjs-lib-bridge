package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu@163.com on 2018/1/31.
 */
public interface FeebackMvpPresenter<M extends AppModel, V extends FeebackMvpView> extends MvpPresenter<V> {

    void sendFeeback();
}
