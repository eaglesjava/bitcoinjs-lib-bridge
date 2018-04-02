package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;

/**
 * Created by isanwenyu@163.com on 2018/4/2.
 */
public class RequestRecordFailEvent extends MessageEvent {

    private Long mWalletId;

    public RequestRecordFailEvent(Long walletId) {
        mWalletId = walletId;
    }

    public Long getWalletId() {
        return mWalletId;
    }

    public RequestRecordFailEvent setWalletId(Long walletId) {
        mWalletId = walletId;
        return this;
    }
}
