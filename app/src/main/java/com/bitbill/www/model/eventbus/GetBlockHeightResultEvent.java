package com.bitbill.www.model.eventbus;

/**
 * Created by isanwenyu@163.com on 2018/4/1.
 */
public class GetBlockHeightResultEvent {

    private long mBlockheight;

    public GetBlockHeightResultEvent(long blockheight) {
        mBlockheight = blockheight;
    }

    public long getBlockheight() {
        return mBlockheight;
    }

    public GetBlockHeightResultEvent setBlockheight(long blockheight) {
        mBlockheight = blockheight;
        return this;
    }
}
