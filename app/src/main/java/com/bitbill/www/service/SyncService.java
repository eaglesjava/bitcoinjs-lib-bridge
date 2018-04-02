/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bitbill.www.common.app.BaseService;
import com.bitbill.www.common.presenter.BtcTxMvpPresenter;
import com.bitbill.www.common.presenter.BtcTxMvpView;
import com.bitbill.www.common.presenter.GetCacheVersionMvpPresenter;
import com.bitbill.www.common.presenter.GetCacheVersionMvpView;
import com.bitbill.www.common.presenter.GetExchangeRateMvpPresenter;
import com.bitbill.www.common.presenter.GetExchangeRateMvpView;
import com.bitbill.www.common.presenter.ParseTxInfoMvpPresenter;
import com.bitbill.www.common.presenter.ParseTxInfoMvpView;
import com.bitbill.www.common.presenter.SyncAddressMvpPresentder;
import com.bitbill.www.common.presenter.SyncAddressMvpView;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.eventbus.AppBackgroundEvent;
import com.bitbill.www.model.eventbus.GetBlockHeightResultEvent;
import com.bitbill.www.model.eventbus.GetCacheVersionEvent;
import com.bitbill.www.model.eventbus.ListUnconfirmEvent;
import com.bitbill.www.model.eventbus.ParsedTxEvent;
import com.bitbill.www.model.eventbus.RefreshExchangeRateEvent;
import com.bitbill.www.model.eventbus.RequestRecordEvent;
import com.bitbill.www.model.eventbus.SyncAddressEvent;
import com.bitbill.www.model.eventbus.SyncLastAddressIndexEvent;
import com.bitbill.www.model.eventbus.TxElementsParseEvent;
import com.bitbill.www.model.eventbus.UpdateConfirmEvent;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.db.entity.TxRecord;
import com.bitbill.www.model.transaction.network.entity.TxElement;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.socket.ContextBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by isanwenyu@163.com on 2017/07/17.
 */
public class SyncService extends BaseService<GetExchangeRateMvpPresenter> implements GetExchangeRateMvpView, GetCacheVersionMvpView, BtcTxMvpView, ParseTxInfoMvpView, SyncAddressMvpView {

    private final String TAG = "SyncService";

    @Inject
    GetExchangeRateMvpPresenter<AppModel, GetExchangeRateMvpView> mGetExchangeRateMvpPresenter;
    @Inject
    ParseTxInfoMvpPresenter<TxModel, ParseTxInfoMvpView> mParseTxInfoMvpPresenter;
    @Inject
    GetCacheVersionMvpPresenter<WalletModel, GetCacheVersionMvpView> mGetCacheVersionMvpPresenter;
    @Inject
    SyncAddressMvpPresentder<AddressModel, SyncAddressMvpView> mSyncAddressMvpPresentder;
    @Inject
    BtcTxMvpPresenter<TxModel, BtcTxMvpView> mBtcTxMvpPresenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncService.class);
        context.startService(starter);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        EventBus.getDefault().register(this);
        //每30秒刷新汇率
        getMvpPresenter().refreshExchangeRate();

        getCacheVersion();
    }

    @Override
    public GetExchangeRateMvpPresenter getMvpPresenter() {
        return mGetExchangeRateMvpPresenter;
    }

    @Override
    public void injectComponent() {
        getServiceComponent().inject(this);
        addPresenter(mParseTxInfoMvpPresenter);
        addPresenter(mGetCacheVersionMvpPresenter);
        addPresenter(mSyncAddressMvpPresentder);
        addPresenter(mBtcTxMvpPresenter);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getCacheVersion() {
        Log.d(TAG, "getCacheVersion() called");
        mGetCacheVersionMvpPresenter.getCacheVersion();
    }

    @Override
    public void getBtcRateSuccess(double cnyRate, double usdRate) {
        Log.d(TAG, "getBtcRateSuccess() called with: cnyRate = [" + cnyRate + "], usdRate = [" + usdRate + "]");
        //通知界面已更新
        EventBus.getDefault().post(new RefreshExchangeRateEvent(cnyRate, usdRate));
    }

    @Override
    public void getResponseAddressIndex(long indexNo, long changeIndexNo, Wallet wallet) {
        Log.d(TAG, "getResponseAddressIndex() called with: indexNo = [" + indexNo + "], changeIndexNo = [" + changeIndexNo + "], wallet = [" + wallet + "]");
        mSyncAddressMvpPresentder.syncLastAddressIndex(indexNo, changeIndexNo, wallet);
    }

    @Override
    public void getDiffVersionWallets(List<Wallet> tmpWalletList) {
        Log.d(TAG, "getDiffVersionWallets() called with: tmpWalletList = [" + tmpWalletList + "]");
        if (!StringUtils.isEmpty(tmpWalletList)) {
            for (Wallet wallet : tmpWalletList) {
                // 只更新更改的wallet的交易记录
                requestTxRecord(wallet);
            }
        }

    }

    @Override
    public void getBlockHeight(long blockheight) {
        Log.d(TAG, "getBlockHeight() called with: blockheight = [" + blockheight + "]");
        getApp().setBlockHeight(blockheight);
        EventBus.getDefault().post(new GetBlockHeightResultEvent(blockheight));
    }

    @Override
    public void getWalletFail(Long walletId) {

    }

    @Override
    public void getTxRecordSuccess(List<TxElement> list, Long walletId) {
        Log.d(TAG, "getTxRecordSuccess() called with: list = [" + list + "], walletId = [" + walletId + "]");
        if (!StringUtils.isEmpty(list)) {
            mParseTxInfoMvpPresenter.parseTxInfo(list, walletId, TAG);
        }

    }

    @Override
    public void getTxRecordFail(Long walletId) {
    }

    @Override
    public void getTxInfoListFail(Long walletId, String TAG) {
        Log.d(this.TAG, "getTxInfoListFail() called with: walletId = [" + walletId + "]");
        EventBus.getDefault().post(new ParsedTxEvent(null, walletId, TAG));
    }

    @Override
    public void parsedTxItemList(List<TxRecord> txRecords, Long walletId, String TAG) {
        Log.d(this.TAG, "parsedTxItemList() called with: txRecords = [" + txRecords + "], walletId = [" + walletId + "]");
        //通知界面解析交易成功
        EventBus.getDefault().post(new ParsedTxEvent(txRecords, walletId, TAG));
    }

    @Override
    public void parsedTxItemListFail(Long walletId, String TAG) {
        Log.d(this.TAG, "parsedTxItemListFail() called with: walletId = [" + walletId + "]");
        EventBus.getDefault().post(new ParsedTxEvent(null, walletId, TAG));
    }


    @Override
    public void syncAddressSuccess(Wallet wallet) {
        Log.d(TAG, "syncAddressSuccess() called with: wallet = [" + wallet + "]");
        //本地index更新成功 重新刷新交易记录
        requestTxRecord(wallet);
        EventBus.getDefault().post(new SyncAddressEvent(wallet));
    }

    private void requestTxRecord(Wallet wallet) {
        Log.d(TAG, "requestTxRecord() called with: wallet = [" + wallet + "]");
        if (mBtcTxMvpPresenter != null) {
            mBtcTxMvpPresenter.requestTxRecord(wallet);
        }
    }


    @Override
    public void listUnconfirmSuccess(List<TxElement> data) {
        Log.d(TAG, "listUnconfirmSuccess() called with: data = [" + data + "]");
        mParseTxInfoMvpPresenter.parseTxInfo(data, null, TAG);
    }

    @Override
    public void listUnconfirmFail() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppBackgroundEvent(AppBackgroundEvent appBackgroundEvent) {
        if (appBackgroundEvent == null) {
            return;
        }
        getMvpPresenter().setAppBackground(appBackgroundEvent.isBackground());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestRecordEvent(RequestRecordEvent requestRecordEvent) {
        if (requestRecordEvent == null) {
            return;
        }
        requestTxRecord(requestRecordEvent.getWallet());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onListUnconfirmEvent(ListUnconfirmEvent listUnconfirmEvent) {
        if (listUnconfirmEvent == null) {
            return;
        }
        requestListBtcUnconfirm();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateConfirmEvent(UpdateConfirmEvent updateConfirmEvent) {
        if (updateConfirmEvent == null) {
            return;
        }
        updateLocalCache(updateConfirmEvent.getContext(), updateConfirmEvent.getWalletList());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncLastAddressIndexEvent(SyncLastAddressIndexEvent syncLastAddressIndexEvent) {
        if (syncLastAddressIndexEvent == null) {
            return;
        }
        getResponseAddressIndex(syncLastAddressIndexEvent.getIndexNo(), syncLastAddressIndexEvent.getChangeIndexNo(), syncLastAddressIndexEvent.getWallet());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTxElementsParseEvent(TxElementsParseEvent txElementsParseEvent) {
        if (txElementsParseEvent == null) {
            return;
        }
        if (mParseTxInfoMvpPresenter != null) {
            mParseTxInfoMvpPresenter.parseTxInfo(txElementsParseEvent.getTxElements(), txElementsParseEvent.getWalletId(), txElementsParseEvent.getTag());

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetBlockHeightEvent(GetCacheVersionEvent getCacheVersionEvent) {
        if (getCacheVersionEvent == null) {
            return;
        }
        getCacheVersion();
    }

    private void requestListBtcUnconfirm() {
        if (mBtcTxMvpPresenter != null) {
            mBtcTxMvpPresenter.listUnconfirm();
        }
    }

    private void updateLocalCache(ContextBean context, List<Wallet> walletList) {
        if (!StringUtils.isEmpty(walletList)) {
            for (Wallet wallet : walletList) {
                if (wallet.getName().equals(context.getWalletId())) {
                    getResponseAddressIndex(context.getIndexNo(), context.getChangeIndexNo(), wallet);
                    if (wallet.getVersion() != context.getVersion()) {
                        List<Wallet> tmpWalletList = new ArrayList<>();
                        tmpWalletList.add(wallet);
                        //重新获取交易列表
                        getDiffVersionWallets(tmpWalletList);
                    }
                }
            }
        }
        if (context.getHeight() > 0) {
            getApp().setBlockHeight(context.getHeight());
        }
    }
}
