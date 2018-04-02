package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;
import com.bitbill.www.model.transaction.network.entity.TxElement;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/4/1.
 */
public class TxElementsParseEvent extends MessageEvent {

    private Long mWalletId;
    private List<TxElement> mTxElements;

    public TxElementsParseEvent(List<TxElement> txElements, Long walletId) {
        mTxElements = txElements;
        mWalletId = walletId;
    }

    public List<TxElement> getTxElements() {
        return mTxElements;
    }

    public TxElementsParseEvent setTxElements(List<TxElement> txElements) {
        mTxElements = txElements;
        return this;
    }

    public Long getWalletId() {
        return mWalletId;
    }

    public TxElementsParseEvent setWalletId(Long walletId) {
        mWalletId = walletId;
        return this;
    }
}
