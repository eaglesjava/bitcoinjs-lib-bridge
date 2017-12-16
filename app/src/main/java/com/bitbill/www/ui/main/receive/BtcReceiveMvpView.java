package com.bitbill.www.ui.main.receive;

import android.graphics.Bitmap;

import com.bitbill.www.common.base.view.MvpView;

/**
 * Created by isanwenyu@163.com on 2017/12/15.
 */
public interface BtcReceiveMvpView extends MvpView {

    void setSendAddress(String address);

    void createAddressQrcodeSuccess(Bitmap qrcodeBitmap);

    void createAddressQrcodeFail();

    void refreshAddressFail();

    void getSelectedWalletFail();

    void refreshAddressSuccess();
}
