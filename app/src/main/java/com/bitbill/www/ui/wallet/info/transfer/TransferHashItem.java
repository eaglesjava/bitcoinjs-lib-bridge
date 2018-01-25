package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.model.entity.TitleItem;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferHashItem extends TitleItem {
    String hash;

    public String getHash() {
        return hash;
    }

    public TransferHashItem setHash(String hash) {
        this.hash = hash;
        return this;
    }
}
