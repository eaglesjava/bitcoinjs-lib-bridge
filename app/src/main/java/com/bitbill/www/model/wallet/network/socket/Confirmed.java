package com.bitbill.www.model.wallet.network.socket;

import com.bitbill.www.common.base.model.entity.Entity;

/**
 * Created by isanwenyu on 2018/1/26.
 */

public class Confirmed extends Entity {

    /**
     * context : {"walletId":"bitpie","amount":"0.01","indexNo":132,"changeIndexNo":114,"type":"RECEIVE","txHash":"7415b5b0c12ef149539ee90c6bb751d808dc3767a3599a19aad0dc92fda77cea","version":37,"height":506045}
     */

    private ContextBean context;

    public ContextBean getContext() {
        return context;
    }

    public void setContext(ContextBean context) {
        this.context = context;
    }

}
