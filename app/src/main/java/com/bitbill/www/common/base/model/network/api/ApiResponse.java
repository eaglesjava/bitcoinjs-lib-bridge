package com.bitbill.www.common.base.model.network.api;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public class ApiResponse<T> {

    /**
     * data : null
     * errorLog : null
     * message : 成功
     * status : 0
     */

    private T data;
    private String errorLog;
    private String message;
    private int status;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
