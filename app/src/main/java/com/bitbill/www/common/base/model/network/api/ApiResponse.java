package com.bitbill.www.common.base.model.network.api;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public class ApiResponse<T> {

    public static final int STATUS_CODE_SUCCESS = 0;//成功
    public static final int STATUS_WALLET_ID_EXSIST = -40;//钱包id已存在
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

    public boolean isSuccess() {
        return status == STATUS_CODE_SUCCESS;
    }

    public boolean isWalletIdExsist() {
        return status == STATUS_WALLET_ID_EXSIST;
    }
}
