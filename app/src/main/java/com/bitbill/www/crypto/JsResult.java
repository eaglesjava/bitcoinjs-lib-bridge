package com.bitbill.www.crypto;

import com.bitbill.www.common.base.model.entity.Entity;

/**
 * Created by isanwenyu on 2018/1/22.
 */

public class JsResult extends Entity {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAIL = -1;

    public int status;
    public String[] data;
    public String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
