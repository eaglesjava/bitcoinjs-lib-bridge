package com.bitbill.www.model.eventbus;

import com.bitbill.www.model.transaction.db.entity.TxRecord;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/4/1.
 */
public class ParsedTxEvent {

    private String mTag;
    private Long mWalletId;
    private List<TxRecord> mTxRecords;

    public ParsedTxEvent(List<TxRecord> txRecords, Long walletId, String TAG) {
        mTxRecords = txRecords;
        mWalletId = walletId;
        mTag = TAG;
    }

    public List<TxRecord> getTxRecords() {
        return mTxRecords;
    }

    public ParsedTxEvent setTxRecords(List<TxRecord> txRecords) {
        mTxRecords = txRecords;
        return this;
    }

    public Long getWalletId() {
        return mWalletId;
    }

    public ParsedTxEvent setWalletId(Long walletId) {
        mWalletId = walletId;
        return this;
    }

    public String getTag() {
        return mTag;
    }

    public ParsedTxEvent setTag(String tag) {
        mTag = tag;
        return this;
    }
}
