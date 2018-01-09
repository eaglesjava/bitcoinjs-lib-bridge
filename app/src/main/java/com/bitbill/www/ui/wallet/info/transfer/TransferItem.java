package com.bitbill.www.ui.wallet.info.transfer;

import java.io.Serializable;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class TransferItem implements Serializable {
    private String title;

    public String getTitle() {
        return title;
    }

    public TransferItem setTitle(String title) {
        this.title = title;
        return this;
    }
}
