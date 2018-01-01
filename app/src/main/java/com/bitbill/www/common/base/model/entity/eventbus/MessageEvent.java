/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.base.model.entity.eventbus;

import com.bitbill.www.common.base.model.entity.Entity;

import java.io.Serializable;

/**
 * Created by isanwenyu@163.com on 2017/7/18.
 */
public class MessageEvent extends Entity {
    /**
     * message state code
     */
    int code;
    String msg;
    Class<?> from;
    Class<?> to;
    Serializable data;

    public MessageEvent() {
    }

    public MessageEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Class<?> getFrom() {
        return from;
    }

    public void setFrom(Class<?> from) {
        this.from = from;
    }

    public Class<?> getTo() {
        return to;
    }

    public void setTo(Class<?> to) {
        this.to = to;
    }

    public Serializable getData() {
        return data;
    }

    public MessageEvent setData(Serializable data) {
        this.data = data;
        return this;
    }
}
