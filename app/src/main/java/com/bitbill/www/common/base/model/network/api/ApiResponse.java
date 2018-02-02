package com.bitbill.www.common.base.model.network.api;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public class ApiResponse<T> {

    public static final int STATUS_CODE_SUCCESS = 0;//服务器忙请稍后再试!
    public static final int STATUS_SERVER_BUSY = -30;//钱包id已存在
    public static final int STATUS_LACK_MADATORY_PARAMS = -31;//缺少必要的参数
    public static final int STATUS_INVALID_PARAM_TYPE = -32;//	无效的参数类型
    public static final int STATUS_WALLET_ID_EXSIST = -40;//钱包id已存在
    public static final int STATUS_WALLET_NO_EXSIST = -41;//钱包id已存在
    public static final int STATUS_IP_EXCEED_LIMIT = -88;//您的IP超过了规定限制
    public static final int STATUS_SERVER_ERROR = 500;//服务器异常
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
