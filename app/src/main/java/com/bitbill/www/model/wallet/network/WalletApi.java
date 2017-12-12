package com.bitbill.www.model.wallet.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceResponse;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;

import io.reactivex.Observable;

/**
 * Created by isanwenyu@163.com on 2017/11/30.
 */
public interface WalletApi extends Api {

    /**
     * 创建钱包
     *
     * @param createWalletRequest
     * @return
     */
    Observable<ApiResponse<String>> createWallet(CreateWalletRequest createWalletRequest);

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    Observable<ApiResponse<String>> importWallet(ImportWalletRequest importWalletRequest);

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    Observable<ApiResponse<String>> checkWalletId(CheckWalletIdRequest checkWalletIdRequest);

    /**
     * 获取walletId
     *
     * @param getWalletIdRequest
     * @return
     */
    Observable<ApiResponse<GetWalletIdResponse>> getWalletId(GetWalletIdRequest getWalletIdRequest);

    /**
     * 查询余额
     *
     * @param getBalanceRequest
     * @return
     */
    Observable<ApiResponse<GetBalanceResponse>> getBalance(GetBalanceRequest getBalanceRequest);


}
