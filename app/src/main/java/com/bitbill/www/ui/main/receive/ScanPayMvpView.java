package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.btc.network.entity.TxElement;

/**
 * Created by isanwenyu on 2017/12/25.
 */

public interface ScanPayMvpView extends MvpView {

    String getReceiveAddress();

    void createReceiveQrcodeSuccess(Bitmap bitmap);

    void createReceiveQrcodeFail();

    String getReceiveAmount();

    String getTxHash();

    void getTxHashFail();

    void addressMatchTx(boolean match, TxElement txElement);
}
