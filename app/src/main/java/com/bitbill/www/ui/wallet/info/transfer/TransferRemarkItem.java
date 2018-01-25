package com.bitbill.www.ui.wallet.info.transfer;

import com.bitbill.www.common.base.model.entity.TitleItem;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferRemarkItem extends TitleItem {
    String remark;

    public String getRemark() {
        return remark;
    }

    public TransferRemarkItem setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}
