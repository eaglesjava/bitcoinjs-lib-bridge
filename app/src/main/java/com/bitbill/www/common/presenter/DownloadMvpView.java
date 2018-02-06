package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;

import java.io.File;

/**
 * Created by isanwenyu on 2018/1/31.
 */

public interface DownloadMvpView extends MvpView {
    void updateProgress(long bytesDownloaded, long totalBytes);

    void downloadFileComplete(File file);

    void downloadFileFail();

    void inValidUrl();
}
