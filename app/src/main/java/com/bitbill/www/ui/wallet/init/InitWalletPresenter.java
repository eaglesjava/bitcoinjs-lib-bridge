package com.bitbill.www.ui.wallet.init;

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.JsResult;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletResponse;
import com.google.gson.JsonSyntaxException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
public class InitWalletPresenter<W extends WalletModel, V extends InitWalletMvpView> extends ModelPresenter<W, V> implements InitWalletMvpPresenter<W, V> {

    private static final String TAG = "InitWalletPresenter";
    private Wallet mWallet;

    @Inject
    public InitWalletPresenter(W model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);

    }

    /**
     * 导入钱包
     */
    @Override
    public void initWallet() {
        //  有效性校验 && 合法性校验
        if (!isValidTradePwd() || !isValidConfirmTradePwd()) {
            return;
        }
        mWallet = getMvpView().getWallet();
        //构建钱包实体
        if (mWallet == null) {
            mWallet = new Wallet();
        }
        mWallet.setTradePwd(getMvpView().getTradePwd());
        //显示加载
        getMvpView().showLoading();
        if (getMvpView().isResetPwd()) {
            resetPwd();
        } else if (getMvpView().isCreateWallet()) {
            mWallet.setName(getMvpView().getWalletId());
            mWallet.setCreatedAt(System.currentTimeMillis());
            mWallet.setCoinType(AppConstants.BTC_COIN_TYPE);
            //生成助记词
            createMnemonic();
        } else {
            mWallet.setName(getMvpView().getWalletId());
            mWallet.setCreatedAt(System.currentTimeMillis());
            mWallet.setCoinType(AppConstants.BTC_COIN_TYPE);
            //调用导入钱包接口
            importWallet();
        }
    }

    /**
     * 获取助记词
     */
    @Override
    public void createMnemonic() {
        if (!isValidWallet()) {
            getMvpView().hideLoading();
            return;
        }
        try {
            BitcoinJsWrapper.getInstance().generateMnemonicCNRetrunSeedHexAndXPublicKey(new BitcoinJsWrapper.Callback() {
                @Override
                public void call(String key, String jsResult) {
                    if (!isViewAttached()) {
                        return;
                    }
                    if (StringUtils.isEmpty(jsResult)) {
                        getMvpView().hideLoading();
                        return;
                    }
                    JsResult result = null;
                    try {
                        result = JsonUtils.deserialize(jsResult, JsResult.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (result.status == JsResult.STATUS_SUCCESS) {
                        String[] data = result.getData();
                        if (data != null && data.length == 4) {
                            Log.d(TAG, "generateMnemonicCNRetrunSeedHexAndXPublicKey: key = [" + key + "], Mnemonic = [" + data[0] + "], seedhex = [" + data[1] + "], extendedPublicKey = [" + data[2] + "], internalPublicKey = [" + data[3] + "]");
                            StringUtils.encryptMnemonicAndSeedHex(data[0], data[1], data[2], data[3], getMvpView().getTradePwd(), mWallet);
                            //调用后台创建钱包接口
                            createWallet();
                        } else {
                            getMvpView().hideLoading();
                            getMvpView().createWalletFail();
                        }
                    } else {
                        getMvpView().hideLoading();
                        getMvpView().createWalletFail();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().hideLoading();
            getMvpView().createWalletFail();
        }

    }

    @Override
    public void createWallet() {

        if (!isValidWallet() || !isValidWalletId() || !isValidXPublicKey()) {
            getMvpView().hideLoading();
            return;
        }

        getCompositeDisposable().add(getModelManager().createWallet(new CreateWalletRequest(mWallet.getName(), mWallet.getExtentedPublicKey(), mWallet.getInternalPublicKey(), getApp().getUUIDMD5(), getDeviceToken()))
                .compose(applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse stringApiResponse) {
                        super.onNext(stringApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.d(TAG, "onNext() called with: stringApiResponse = [" + stringApiResponse + "]");
                        if (stringApiResponse != null) {
                            int status = stringApiResponse.getStatus();
                            if (status == ApiResponse.STATUS_CODE_SUCCESS) {
                                insertWallet();
                            } else if (status == ApiResponse.STATUS_WALLET_ID_EXSIST) {
                                getMvpView().showMessage(getApp().getString(R.string.error_wallet_id_exsist));
                            } else {
                                getMvpView().createWalletFail();
                            }
                        } else {
                            getMvpView().createWalletFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.e(TAG, "onError: ", e);
                        // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
                        }

                    }
                }));

    }

    @Override
    public void importWallet() {
        if (!isValidWallet() || !isValidWalletId() || !isValidXPublicKey()) {
            getMvpView().hideLoading();
            return;
        }
        getCompositeDisposable().add(getModelManager()
                .importWallet(new ImportWalletRequest(mWallet.getName(), mWallet.getExtentedPublicKey(), mWallet.getInternalPublicKey(), getApp().getUUIDMD5(), getDeviceToken()))
                .compose(applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<ImportWalletResponse>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<ImportWalletResponse> importWalletResponseApiResponse) {
                        super.onNext(importWalletResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.d(TAG, "onNext() called with: importWalletResponseApiResponse = [" + importWalletResponseApiResponse + "]");
                        if (importWalletResponseApiResponse != null && importWalletResponseApiResponse.isSuccess()) {
                            //完善wallet相关属性
                            StringUtils.encryptMnemonicAndSeedHex(mWallet.getMnemonic(), mWallet.getSeedHex(), mWallet.getExtentedPublicKey(), mWallet.getInternalPublicKey(), getMvpView().getTradePwd(), mWallet);
                            insertWallet();
                            ImportWalletResponse data = importWalletResponseApiResponse.getData();
                            if (data != null) {
                                getMvpView().getResponseAddressIndex(data.getIndexNo(), data.getChangeIndexNo());
                            }


                        } else {
                            getMvpView().createWalletFail();
                        }
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "onError: ", e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
                        }

                    }
                }));

    }

    @Override
    public void insertWallet() {
        if (!isValidWallet()) {
            getMvpView().hideLoading();
            return;
        }
        //  插入操作只有创建成功后
        getCompositeDisposable().add(getModelManager()
                .insertWallet(mWallet)
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Long>() {
                    @Override
                    public void onNext(Long id) {
                        super.onNext(id);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (id != null) {
                            getMvpView().createWalletSuccess(mWallet);
                        } else {
                            getMvpView().createWalletFail();
                        }
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "initWalletFail ", e);
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        getMvpView().createWalletFail();


                    }
                }));
    }


    @Override
    public void resetPwd() {
        if (!isValidWallet()) {
            getMvpView().hideLoading();
            return;
        }
        Wallet wallet = getMvpView().getWallet();
        String mnemonic = wallet.getMnemonic();
        // 校验助记词是否正确
        BitcoinJsWrapper.getInstance().validateMnemonicReturnSeedHexAndXPublicKey(mnemonic, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String jsResult) {
                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().resetPwdFail();
                    getMvpView().hideLoading();
                    return;
                }
                JsResult result = null;
                try {
                    result = JsonUtils.deserialize(jsResult, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    getMvpView().resetPwdFail();
                    getMvpView().hideLoading();
                    return;
                }
                if (result.status == JsResult.STATUS_SUCCESS) {
                    String[] data = result.getData();
                    if (data == null) {
                        getMvpView().resetPwdFail();
                        getMvpView().hideLoading();
                        return;
                    }
                    if ("true".equals(data[0]) && data.length >= 4) {

                        //更新wallet对象
                        String seedHex = data[1];
                        String extendedPublicKey = data[2];
                        String internalPublicKey = data[3];
                        StringUtils.encryptMnemonicAndSeedHex(mnemonic, seedHex, extendedPublicKey, internalPublicKey, getMvpView().getTradePwd(), wallet);
                        //不需要备份
                        wallet.setIsBackuped(true);
                        //更新数据库
                        getCompositeDisposable().add(getModelManager()
                                .updateWallet(wallet)
                                .compose(applyScheduler())
                                .subscribeWith(new BaseSubcriber<Boolean>() {
                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        super.onNext(aBoolean);
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        getMvpView().hideLoading();
                                        if (aBoolean) {
                                            getMvpView().resetPwdSuccess();
                                        } else {
                                            getMvpView().resetPwdFail();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        getMvpView().resetPwdFail();
                                        getMvpView().hideLoading();
                                    }
                                }));
                    } else {
                        getMvpView().resetPwdFail();
                        getMvpView().hideLoading();
                    }
                } else {
                    getMvpView().resetPwdFail();
                    getMvpView().hideLoading();
                }
            }
        });

    }

    private boolean isValidWalletId() {
        // Check for a valid wallet id.
        if (!StringUtils.isWalletIdValid(getMvpView().getWalletId())) {
            getMvpView().invalidWalletId();
            return false;
        }
        return true;
    }

    private boolean isValidTradePwd() {
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(getMvpView().getTradePwd())) {
            getMvpView().requireTradePwd();
            return false;
        }
        if (!StringUtils.isPasswordValid(getMvpView().getTradePwd())) {
            getMvpView().invalidTradePwd();
            return false;
        }
        return true;
    }

    private boolean isValidConfirmTradePwd() {
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(getMvpView().getConfirmTradePwd())) {
            getMvpView().requireTradeConfirmPwd();
            return false;
        }
        if (!StringUtils.isPasswordValid(getMvpView().getConfirmTradePwd())) {
            getMvpView().isPwdInConsistent();
            return false;
        }
        if (!isPwdConsistent()) {
            getMvpView().isPwdInConsistent();
            return false;
        }
        return true;
    }


    private boolean isPwdConsistent() {
        return TextUtils.equals(getMvpView().getTradePwd(), getMvpView().getConfirmTradePwd());
    }


    private boolean isValidWallet() {
        if (mWallet == null) {
            getMvpView().initWalletInfoFail();
            return false;
        }
        return true;
    }

    /**
     * @return todo 使用能够极光唯一标识
     */
    public String getDeviceToken() {
        return "";
    }

    public boolean isValidXPublicKey() {
        if (StringUtils.isEmpty(mWallet.getExtentedPublicKey())) {
            getMvpView().initWalletInfoFail();
            return false;
        }
        return true;
    }
}
