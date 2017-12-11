package com.bitbill.www.ui.main.receive;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu@163.com on 2017/12/11.
 */
public interface ReceiveMvpPresenter<M extends AppModel, V extends ReceiveMvpView> extends MvpPresenter<V> {

    void setRemindDialogShown();

    boolean isRemindDialogShown();
}
