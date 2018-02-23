package com.bitbill.www.model.app.network.entity;

/**
 * Created by isanwenyu on 2018/1/31.
 */

public class FeedBackRequest {
    private String context;
    private String contact;

    public FeedBackRequest(String context, String contact) {
        this.context = context;
        this.contact = contact;
    }
}
