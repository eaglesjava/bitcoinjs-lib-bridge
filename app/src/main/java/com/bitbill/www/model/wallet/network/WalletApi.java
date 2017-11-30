package com.bitbill.www.model.wallet.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public interface WalletApi extends Api {
    Observable<ApiResponse<String>> createWallet(String walletId, String extendedKeys, String clientId, String deviceToken, long indexNo);
}
