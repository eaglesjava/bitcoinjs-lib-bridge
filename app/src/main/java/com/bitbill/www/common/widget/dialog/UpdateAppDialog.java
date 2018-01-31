/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.bitbill.www.common.widget.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.presenter.DownloadMvpPresenter;
import com.bitbill.www.common.presenter.DownloadMvpView;
import com.bitbill.www.common.utils.UIHelper;
import com.bitbill.www.di.component.ActivityComponent;
import com.bitbill.www.model.app.AppModel;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by janisharali on 21/03/17.
 */

public class UpdateAppDialog extends BaseDialog implements UpdateAppMvpView, DownloadMvpView {

    private static final String TAG = "RateUsDialog";

    @Inject
    DownloadMvpPresenter<AppModel, DownloadMvpView> mPresenter;
    private String mTitle;
    private ProgressDialog mProgressDialog;

    public static UpdateAppDialog newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(AppConstants.ARG_TITLE, title);
        UpdateAppDialog fragment = new UpdateAppDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadMvpPresenter<AppModel, DownloadMvpView> getMvpPresenter() {
        return mPresenter;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(AppConstants.ARG_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityComponent component = getActivityComponent();
        if (component != null) {

            component.inject(this);
            if (mPresenter != null) {
                mPresenter.onAttach(this);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle(mTitle);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (getMvpPresenter() != null) {
                    getMvpPresenter().downloadFile(AppConstants.DOWNLOAD_APK_URL, "bitbill.apk");
                }
            }
        });
        return mProgressDialog;
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
        super.onDestroyView();
    }

    @Override
    public void updateProgress(long bytesDownloaded, long totalBytes) {
        long progress = bytesDownloaded * 100 / totalBytes;
        mProgressDialog.setMax((int) (totalBytes / (1024.00 * 1024.00)));
        mProgressDialog.setProgress((int) (bytesDownloaded / (1024.00 * 1024.00)));
        mProgressDialog.setProgressNumberFormat("%1dM/%2dM");
    }

    @Override
    public void downloadFileComplete(File file) {
        //安装apk
        UIHelper.installApk(getContext(), file);
        //关闭当前对话框
        dismissDialog(TAG);
    }

    @Override
    public void downloadFileFail() {
        onError(R.string.fail_update_install_apk);
        //关闭当前对话框
        dismissDialog(TAG);
    }

}