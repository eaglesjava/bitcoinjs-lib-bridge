package com.bitbill.www.ui.main.my;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu@163.com on 2017/12/31.
 */
public interface ShortCutSettingMvpPresenter<M extends AppModel, V extends MvpView> extends MvpPresenter<V> {
    boolean isShortcutShown();

    void setShortcutShown(boolean shown);
}
