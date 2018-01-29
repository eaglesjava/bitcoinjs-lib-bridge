package com.bitbill.www.model.wallet.network;

import com.bitbill.www.common.base.model.network.api.Api;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.model.transaction.network.entity.GetTxInfoRequest;
import com.bitbill.www.model.transaction.network.entity.GetTxInfoResponse;
import com.bitbill.www.model.wallet.network.entity.CheckWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.DeleteWalletRequest;
import com.bitbill.www.model.wallet.network.entity.GetBalanceRequest;
import com.bitbill.www.model.wallet.network.entity.GetCacheVersionRequest;
import com.bitbill.www.model.wallet.network.entity.GetConfigResponse;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdRequest;
import com.bitbill.www.model.wallet.network.entity.GetWalletIdResponse;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletResponse;

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
    Observable<ApiResponse> createWallet(CreateWalletRequest createWalletRequest);

    /**
     * 导入钱包
     *
     * @param importWalletRequest
     * @return
     */
    Observable<ApiResponse<ImportWalletResponse>> importWallet(ImportWalletRequest importWalletRequest);

    /**
     * 删除钱包
     *
     * @param deleteWalletRequest
     * @return
     */
    Observable<ApiResponse> deleteWallet(DeleteWalletRequest deleteWalletRequest);

    /**
     * 检查WalletId
     *
     * @param checkWalletIdRequest
     * @return
     */
    Observable<ApiResponse> checkWalletId(CheckWalletIdRequest checkWalletIdRequest);

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
    Observable<ApiResponse> getBalance(GetBalanceRequest getBalanceRequest);


    /**
     * 获取配置信息
     *
     * @return
     */
    Observable<ApiResponse<GetConfigResponse>> getConfig();

    /**
     * 获取交易详情
     *
     * @param getTxInfoRequest
     * @return
     */
    Observable<ApiResponse<GetTxInfoResponse>> getTxInfo(GetTxInfoRequest getTxInfoRequest);


    /**
     * 获取钱包缓存版本号
     *
     * @param getCacheVersionRequest
     * @return
     */
    Observable<ApiResponse> getCacheVersion(GetCacheVersionRequest getCacheVersionRequest);


}
