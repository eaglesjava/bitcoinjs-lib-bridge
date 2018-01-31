package com.bitbill.www.common.presenter;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.FileUitls;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.app.AppModel;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2018/1/31.
 */

@PerActivity
public class DownloadPresenter<M extends AppModel, V extends DownloadMvpView> extends ModelPresenter<M, V> implements DownloadMvpPresenter<M, V> {
    private static final String TAG = "DownloadPresenter";

    @Inject
    public DownloadPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void downloadFile(String url, String fileName) {

        String rootDirPath = FileUitls.getRootDirPath(getApp());
        getModelManager().downloadFile(url, rootDirPath, fileName, new DownloadProgressListener() {
            @Override
            public void onProgress(long bytesDownloaded, long totalBytes) {
                if (!isViewAttached()) {
                    return;
                }
                getMvpView().updateProgress(bytesDownloaded, totalBytes);
            }
        }, new DownloadListener() {
            @Override
            public void onDownloadComplete() {
                if (!isViewAttached()) {
                    return;
                }
                File file = new File(rootDirPath + File.separator + fileName);
                if (file.exists()) {
                    getMvpView().downloadFileComplete(file);
                } else {
                    getMvpView().downloadFileFail();
                }

            }

            @Override
            public void onError(ANError anError) {
                if (!isViewAttached()) {
                    return;
                }
                handleApiError(anError);
                getMvpView().downloadFileFail();
            }
        });

    }
}
