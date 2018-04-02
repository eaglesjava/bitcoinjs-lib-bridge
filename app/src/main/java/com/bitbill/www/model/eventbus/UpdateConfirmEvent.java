package com.bitbill.www.model.eventbus;

import com.bitbill.www.common.base.model.entity.eventbus.MessageEvent;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.socket.ContextBean;

import java.util.List;

/**
 * Created by isanwenyu@163.com on 2018/4/1.
 */
public class UpdateConfirmEvent extends MessageEvent {

    private List<Wallet> mWalletList;
    private ContextBean mContext;

    public UpdateConfirmEvent(ContextBean context, List<Wallet> walletList) {
        mContext = context;
        mWalletList = walletList;
    }

    public ContextBean getContext() {
        return mContext;
    }

    public void setContext(ContextBean context) {
        mContext = context;
    }

    public List<Wallet> getWalletList() {
        return mWalletList;
    }

    public UpdateConfirmEvent setWalletList(List<Wallet> walletList) {
        mWalletList = walletList;
        return this;
    }
}
