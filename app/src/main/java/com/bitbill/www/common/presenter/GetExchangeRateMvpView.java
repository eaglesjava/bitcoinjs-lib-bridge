package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu@163.com on 2018/1/29.
 */
public interface GetExchangeRateMvpView extends MvpView {

    void getBtcRateSuccess(double cnyRate, double usdRate);
}
