package com.bitbill.www.ui.wallet.init;

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.bitbill.www.R;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.DeviceUtil;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.model.wallet.WalletModel;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.bitbill.www.model.wallet.network.entity.CreateWalletRequest;
import com.bitbill.www.model.wallet.network.entity.ImportWalletRequest;
import com.bitbill.www.model.wallet.network.socket.Register;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

import static com.bitbill.www.app.AppConstants.PLATFORM;

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
        mWallet.setCreatedAt(System.currentTimeMillis());
        mWallet.setUpdatedAt(System.currentTimeMillis());
        mWallet.setName(getMvpView().getWalletId());
        mWallet.setTradePwd(getMvpView().getTradePwd());
        //显示加载
        getMvpView().showLoading();
        if (getMvpView().isCreateWallet()) {
            //生成助记词
            createMnemonic();
        } else {
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
                public void call(String key, String... jsResult) {
                    try {
                        if (jsResult != null && jsResult.length > 1) {

                            Log.d(TAG, "generateMnemonicCNRetrunSeedHexAndXPublicKey: key = [" + key + "], Mnemonic = [" + jsResult[0] + "], seedhex = [" + jsResult[1] + "]");
                            StringUtils.encryptMnemonicAndSeedHex(jsResult[0], jsResult[1], jsResult[2], getMvpView().getTradePwd(), mWallet);
                            //调用后台创建钱包接口
                            createWallet();
                        } else {
                            getMvpView().hideLoading();
                            getMvpView().createWalletFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

        getCompositeDisposable().add(getModelManager().createWallet(new CreateWalletRequest(mWallet.getName(), mWallet.getXPublicKey(), DeviceUtil.getDeviceId(), getDeviceToken()))
                .compose(applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<String>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<String> stringApiResponse) {
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
                .importWallet(new ImportWalletRequest(mWallet.getName(), mWallet.getXPublicKey(), DeviceUtil.getDeviceId(), getDeviceToken()))
                .compose(applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<String>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<String> stringApiResponse) {
                        super.onNext(stringApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        Log.d(TAG, "onNext() called with: stringApiResponse = [" + stringApiResponse + "]");
                        if (stringApiResponse != null && stringApiResponse.getStatus() == ApiResponse.STATUS_CODE_SUCCESS) {
                            //完善wallet相关属性
                            StringUtils.encryptMnemonicAndSeedHex(mWallet.getMnemonic(), mWallet.getSeedHex(), mWallet.getXPublicKey(), getMvpView().getTradePwd(), mWallet);
                            insertWallet();
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
                .concatMap(new Function<Long, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Long aLong) throws Exception {
                        //第一个钱包自动设置为默认钱包
                        mWallet.setIsDefault(aLong == 1l);
                        Log.d(TAG, "initWalletSuccess id = [" + aLong + "]");
                        return getModelManager().updateWallet(mWallet);
                    }
                })
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aboolean) {
                        super.onNext(aboolean);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (aboolean) {
                            getMvpView().createWalletSuccess();
                            //注册钱包
                            getApp().registerWallet(new Register(mWallet.getName(), "", DeviceUtil.getDeviceId(), PLATFORM));
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
        if (StringUtils.isEmpty(mWallet.getXPublicKey())) {
            getMvpView().initWalletInfoFail();
            return false;
        }
        return true;
    }
}
