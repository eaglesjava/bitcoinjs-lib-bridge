package com.bitbill.www.common.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bitbill.www.common.app.ScreenshotContentObserver;
import com.bitbill.www.common.base.presenter.MvpPresenter;

import java.io.File;

/**
 * Created by isanwenyu on 2018/1/15.
 */

public abstract class ScreenShotObserverActivity<P extends MvpPresenter> extends BaseToolbarActivity<P> implements ScreenshotContentObserver.OnScreenShotChangeListener {

    private ScreenshotContentObserver mScreenshotContentObserver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenshotContentObserver = new ScreenshotContentObserver(this);
        mScreenshotContentObserver.setOnScreenShotChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mScreenshotContentObserver.startObserve();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScreenshotContentObserver.stopObserve();
    }

    @Override
    public void onScreenShot(File shotFile) {

    }
}
