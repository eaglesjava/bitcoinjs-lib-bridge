package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu@163.com on 2018/1/29.
 */
public interface GetExchangeRateMvpPresenter<M extends AppModel, V extends GetExchangeRateMvpView> extends MvpPresenter<V> {
    void getExchangeRate();

    void refreshExchangeRate();

    void setAppBackground(boolean appBackground);
}
