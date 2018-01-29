package com.bitbill.www.ui.splash;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu@163.com on 2017/12/1.
 */
public interface SplashMvpPresenter<M extends AppModel, V extends SplashMvpView> extends MvpPresenter<V> {
    /**
     * 本地是否有钱包
     */
    void hasWallet();

    String getContactKey();

    boolean isAliasSeted();

    void setAliasSeted(boolean isSeted);

    void initLanguage();
}
