package com.bitbill.www.ui.wallet.init;

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.widget.PwdStatusView;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.JsResult;
import com.bitbill.www.model.app.AppModel;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletResponse;
import com.google.gson.JsonSyntaxException;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu@163.com on 2017/11/17.
 */
public class InitWalletPresenter<W extends WalletModel, V extends InitWalletMvpView> extends ModelPresenter<W, V> implements InitWalletMvpPresenter<W, V> {

    private static final String TAG = "InitWalletPresenter";
    @Inject
    AppModel mAppModel;
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
            int isCN = mAppModel.getCurrentLocale() != null && mAppModel.getCurrentLocale().getLanguage().equals(Locale.ENGLISH.getLanguage()) ? 0 : 1;
            BitcoinJsWrapper.getInstance().generateMnemonicRetrunSeedHexAndXPublicKey(isCN, new BitcoinJsWrapper.Callback() {
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
                            Log.d(TAG, "generateMnemonicRetrunSeedHexAndXPublicKey: key = [" + key + "], Mnemonic = [" + data[0] + "], seedhex = [" + data[1] + "], extendedPublicKey = [" + data[2] + "], internalPublicKey = [" + data[3] + "]");
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
                        if (handleApiResponse(stringApiResponse)) {
                            return;
                        }
                        if (stringApiResponse.isSuccess()) {
                            insertWallet();
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

    /**
     * 一、密码长度:
     * 5 分: 小于等于 4 个字符;10 分: 5 到 7 字符;25 分: 大于 等于 8 个字符
     * 二、字母:
     * 0 分: 没有字母
     * 10 分: 全都是小(大)写字 母;20 分: 大小写混合字母 三、数字:
     * 0分: 没有数字;10分: 1 个数 字;20分: 大于等于 3个数字 四、符号:
     * 0分: 没有符号;10分: 1个符 号;25分: 大于1个符号 五、奖励:
     * 2分: 字母和数字
     * 3分: 字母、数字和符号;5分: 大小写字母、数字和符号 规则:
     * >= 60: 高
     * >= 50: 中
     * >= 0: 低
     *
     * @param pwd
     */
    @Override
    public void complutePwdStrongLevel(String pwd) {
        if (!isViewAttached()) {
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            getMvpView().setPwdStrongLevel(PwdStatusView.StrongLevel.DEFAULT);
            return;
        }

        int score = 0;

        //length
        int length = pwd.length();
        //too long
        if (length > 20) return;

        if (length >= 8) {
            score = 25;
        } else if (length > 4 && length < 8) {
            score = 10;
        } else {
            score = 5;
        }
        //count type
        int[] counts = StringUtils.countType(pwd);
        if (counts == null) {
            return;
        }
        int numberCount = counts[0];
        int upperCount = counts[1];
        int lowerCount = counts[2];
        int specialCount = counts[3];

        //character
        boolean isUpper = upperCount == length;
        boolean isLower = lowerCount == length;
        if (isUpper || isLower) {
            score += 10;
        } else if (lowerCount > 0 && upperCount > 0) {
            score += 20;
        }
        //number
        if (numberCount >= 3) {
            score += 20;
        } else if (numberCount > 0) {
            score += 10;
        }
        //special
        if (specialCount > 1) {
            score += 25;
        } else if (specialCount == 1) {
            score += 10;
        }
        //reward
        if (numberCount > 0 && +lowerCount > 0 && upperCount > 0 && specialCount > 0) {
            score += 5;
        } else if (lowerCount + upperCount > 0 && numberCount > 0 && specialCount > 0) {
            score += 3;
        } else if (lowerCount + upperCount > 0 && numberCount > 0) {
            score += 2;
        }
        Log.d(TAG, "complutePwdStrongLevel() called with: pwd = [" + pwd + "]" + ",score:" + score + ",upperCount:" + upperCount + ",lowerCount:" + lowerCount + ",numberCount:" + numberCount + ",specialCount:" + specialCount);

        if (score >= 60) {
            getMvpView().setPwdStrongLevel(PwdStatusView.StrongLevel.STRONG);
        } else if (score >= 50) {
            getMvpView().setPwdStrongLevel(PwdStatusView.StrongLevel.WEAK);
        } else {
            getMvpView().setPwdStrongLevel(PwdStatusView.StrongLevel.DANGEROUS);
        }
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
