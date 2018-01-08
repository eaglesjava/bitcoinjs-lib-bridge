package com.bitbill.www.common.presenter;

import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.address.AddressModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.GetCacheVersionRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2018/1/6.
 */

@PerActivity
public class GetCacheVersionPresenter<M extends WalletModel, V extends GetCacheVersionMvpView> extends ModelPresenter<M, V> implements GetCacheVersionMvpPresenter<M, V> {
    @Inject
    AddressModel mAddressModel;

    @Inject
    public GetCacheVersionPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getCacheVersion() {
        getCompositeDisposable().add(getModelManager().getAllWallets()
                .concatMap(wallets -> {

                    String extendedKeysHash = "";
                    for (int i = 0; i < wallets.size(); i++) {
                        // check xpubkey
                        String xPublicKey = wallets.get(i).getXPublicKey();
                        extendedKeysHash += StringUtils.isEmpty(xPublicKey) ? "" : EncryptUtils.encryptMD5ToString(xPublicKey);
                        if (i < wallets.size() - 1) {
                            extendedKeysHash += "|";
                        }
                    }
                    return Observable.zip(getModelManager()
                                    .getCacheVersion(new GetCacheVersionRequest(extendedKeysHash))
                            , Observable.just(wallets)
                            , (jsonObjectApiResponse, walletList)
                                    -> new CombineCacheResponse(walletList, jsonObjectApiResponse));

                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<CombineCacheResponse>() {
                    @Override
                    public void onNext(CombineCacheResponse combineCacheResponse) {
                        super.onNext(combineCacheResponse);
                        if (combineCacheResponse != null) {
                            ApiResponse jsonObjectApiResponse = combineCacheResponse.getJsonObjectApiResponse();
                            List<Wallet> wallets = combineCacheResponse.getWallets();
                            if (jsonObjectApiResponse.isSuccess()) {
                                try {
                                    JSONObject data = new JSONObject(String.valueOf(jsonObjectApiResponse.getData()));
                                    jsonObjectApiResponse.getData();
                                    if (data != null && !StringUtils.isEmpty(wallets)) {
                                        getApp().setBlockHeight(data.getLong("blockheight"));
                                        // TODO: 2018/1/6 缓存blockheight
                                        List<Wallet> tmpWalletList = new ArrayList<>();
                                        for (Wallet wallet : wallets) {
                                            JSONObject amountJsonObj = data.getJSONObject(wallet.getName());
                                            long indexNo = amountJsonObj.getLong("indexNo");
                                            if (indexNo > 0) {
                                                getMvpView().getResponseAddressIndex(indexNo, wallet);
                                            }
                                            long version = amountJsonObj.getLong("version");
                                            if (wallet.getVersion() != version) {
                                                wallet.setVersion(version);
                                                tmpWalletList.add(wallet);
                                            }
                                        }
                                        if (tmpWalletList.size() > 0) {
                                            updateWalletList(tmpWalletList);
                                            getMvpView().getDiffVersionWallets(tmpWalletList);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private void updateWalletList(List<Wallet> walletList) {
        getCompositeDisposable().add(getModelManager().updateWallets(walletList)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }));
    }

    private class CombineCacheResponse {
        private List<Wallet> mWallets;
        private ApiResponse jsonObjectApiResponse;

        public CombineCacheResponse(List<Wallet> wallets, ApiResponse jsonObjectApiResponse) {
            mWallets = wallets;
            this.jsonObjectApiResponse = jsonObjectApiResponse;
        }

        public List<Wallet> getWallets() {
            return mWallets;
        }

        public CombineCacheResponse setWallets(List<Wallet> wallets) {
            mWallets = wallets;
            return this;
        }

        public ApiResponse getJsonObjectApiResponse() {
            return jsonObjectApiResponse;
        }

        public CombineCacheResponse setJsonObjectApiResponse(ApiResponse jsonObjectApiResponse) {
            this.jsonObjectApiResponse = jsonObjectApiResponse;
            return this;
        }
    }
}
