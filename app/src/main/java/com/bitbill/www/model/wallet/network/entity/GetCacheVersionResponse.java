package com.bitbill.www.model.wallet.network.entity;

/**
 * Created by isanwenyu on 2017/12/29.
 */

public class GetCacheVersionResponse {

    /**
     * version : 0
     * blockheight : 501239
     */

    private int version;
    private int blockheight;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(int blockheight) {
        this.blockheight = blockheight;
    }
}
