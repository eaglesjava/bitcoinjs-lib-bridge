package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.app.AppModel;

/**
 * Created by isanwenyu on 2018/1/31.
 */

public interface DownloadMvpPresenter<M extends AppModel, V extends DownloadMvpView> extends MvpPresenter<V> {

    void downloadFile(String url, String fileName);
}
