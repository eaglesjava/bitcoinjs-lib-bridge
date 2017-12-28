package com.bitbill.www.ui.main.my;

import android.graphics.Bitmap;

import com.bitbill.www.common.base.view.MvpView;
import com.bitbill.www.model.wallet.db.entity.Wallet;

/**
 * Created by isanwenyu on 2017/12/28.
 */

public interface WalletDetailMvpView extends MvpView {
    String getWalletId();

    void createQrcodeSuccess(Bitmap bitmap);

    void createQrcodeFail();

    Wallet getWallet();

    void deleteWalletSuccess();

    void deleteWalletFail();

    void getWalletInfoFail();
}
