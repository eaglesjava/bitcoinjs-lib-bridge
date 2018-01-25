package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.model.entity.TitleItem;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferDateItem extends TitleItem {
    private String date;

    public String getDate() {
        return date;
    }

    public TransferDateItem setDate(String date) {
        this.date = date;
        return this;
    }
}
