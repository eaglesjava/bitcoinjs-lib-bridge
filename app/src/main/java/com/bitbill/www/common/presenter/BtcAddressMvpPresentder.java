package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.model.address.AddressModel;

/**
 * Created by isanwenyu on 2017/12/19.
 */

public interface BtcAddressMvpPresentder<M extends AddressModel, V extends BtcAddressMvpView> extends MvpPresenter<V> {

    void loadAddress();

    /**
     * 刷新地址
     */
    void refreshAddress();

}
