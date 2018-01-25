package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.model.entity.TitleItem;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferConfirmItem extends TitleItem {
    private long height;

    public TransferConfirmItem(long height) {
        this.height = height;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }
}
