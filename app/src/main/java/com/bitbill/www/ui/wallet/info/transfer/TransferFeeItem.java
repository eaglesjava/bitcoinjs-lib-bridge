package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.model.entity.TitleItem;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferFeeItem extends TitleItem {
    long fee;

    public TransferFeeItem(Long fee) {
        this.fee = fee;
    }

    public long getFee() {
        return fee;
    }

    public TransferFeeItem setFee(long fee) {
        this.fee = fee;
        return this;
    }
}
