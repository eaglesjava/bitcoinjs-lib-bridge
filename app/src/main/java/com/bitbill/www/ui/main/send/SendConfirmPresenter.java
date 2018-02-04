package com.bitbill.www.ui.main.send;

import android.text.TextUtils;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.bitbill.www.common.base.model.network.api.ApiResponse;
import com.bitbill.www.common.base.presenter.ModelPresenter;
import com.bitbill.www.common.rx.BaseSubcriber;
import com.bitbill.www.common.rx.SchedulerProvider;
import com.bitbill.www.common.utils.JsonUtils;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.crypto.BitcoinJsWrapper;
import com.bitbill.www.crypto.CryptoConstants;
import com.bitbill.www.crypto.JsResult;
import com.bitbill.www.crypto.entity.Transaction;
import com.bitbill.www.crypto.utils.ConvertUtils;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.di.scope.PerActivity;
import com.bitbill.www.model.transaction.TxModel;
import com.bitbill.www.model.transaction.network.entity.GetTxElement;
import com.bitbill.www.model.transaction.network.entity.GetTxElementResponse;
import com.bitbill.www.model.transaction.network.entity.SendTransactionRequest;
import com.bitbill.www.model.transaction.network.entity.SendTransactionResponse;
import com.bitbill.www.model.wallet.db.entity.Wallet;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by isanwenyu on 2017/12/18.
 */
@PerActivity
public class SendConfirmPresenter<M extends TxModel, V extends SendConfirmMvpView> extends ModelPresenter<M, V> implements SendConfirmMvpPresenter<M, V> {

    private static final String TAG = "SendConfirmPresenter";

    @Inject
    public SendConfirmPresenter(M model, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(model, schedulerProvider, compositeDisposable);
    }

    @Override
    public void requestListUnspent() {

        if (!isValidWallet()) return;

        Wallet wallet = getMvpView().getWallet();
        String encryptMD5ToString = EncryptUtils.encryptMD5ToString(wallet.getExtentedPublicKey());
        getCompositeDisposable().add(getModelManager()
                .getTxElement(new GetTxElement(encryptMD5ToString))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<GetTxElementResponse>>(getMvpView()) {
                    @Override
                    public void onNext(ApiResponse<GetTxElementResponse> listUnspentResponseApiResponse) {
                        super.onNext(listUnspentResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (listUnspentResponseApiResponse != null && listUnspentResponseApiResponse.isSuccess()) {
                            GetTxElementResponse data = listUnspentResponseApiResponse.getData();
                            if (data != null) {
                                List<GetTxElementResponse.UtxoBean> unspentList = data.getUtxo();
                                if (!StringUtils.isEmpty(unspentList)) {
                                    //获取utxo成功
                                    getMvpView().getTxElementSuccess(unspentList, data.getFees());
                                } else {
                                    getMvpView().amountNoEnough();
                                }
                            } else {
                                getMvpView().getTxElementFail();
                            }

                        } else {
                            getMvpView().getTxElementFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void buildTransaction() {
        // TODO: 2017/12/19 check valid params
        if (!isValidWallet() || !isValidTradePwd() || !isValidUnspendList()) return;
        Wallet wallet = getMvpView().getWallet();
        //check pwd
        String pwd = getMvpView().getTradePwd();
        String seedHex = StringUtils.decryptByPwd(wallet.getEncryptSeed(), pwd);
        List<GetTxElementResponse.UtxoBean> unspentList = new ArrayList<>();
        unspentList.clear();
        unspentList.addAll(getMvpView().getUnspentList());
        //计算手续费 余额从小大排列
        Collections.sort(unspentList, (o1, o2) -> {
            //倒序排列
            return o2.getSumOutAmount() - o1.getSumOutAmount();
        });

        //发送全部不需要计算找零地址
        boolean isSendAll = getMvpView().isSendAll();
        long feeByte = getMvpView().getFeeByte();
        long sendAmount = getMvpView().getSendAmount();
        String sendAddress = getMvpView().getSendAddress();
        long amount = 0;
        int index = -1;
        long fee = 0;
        for (int i = 0; i < unspentList.size(); i++) {
            GetTxElementResponse.UtxoBean unspent = unspentList.get(i);
            fee = (CryptoConstants.INPUT_SIZE * (i + 1) + CryptoConstants.OUTPUT_SIZE * (isSendAll ? 1 : 2)) * feeByte;
            amount += unspent.getSumOutAmount();
            if (!isSendAll && amount - fee >= sendAmount) {
                index = i;
                break;
            }
        }
        if (isSendAll) {
            index = unspentList.size() - 1;
        }
        //check amount
        if (amount <= 0 || index == -1 || fee == 0) {
            getMvpView().amountNoEnough();
            return;
        }
        //check mini fee
        if (fee < CryptoConstants.BTC_MIN_FEE) {
            fee = CryptoConstants.BTC_MIN_FEE;
        }
        //check send amount
        if (amount - fee <= 0) {
            getMvpView().amountNoEnough();
            return;
        }
        //组装交易
        List<Transaction.Input> inputs = new ArrayList<>();
        String inAddress = "";
        for (int i = 0; i <= index; i++) {
            GetTxElementResponse.UtxoBean unspent = unspentList.get(i);
            inputs.add(new Transaction.Input(unspent.getTxid(), unspent.getVIndex(), unspent.getAddressIndex(), unspent.getAddressType() == 1));
            inAddress += unspent.getAddressTxt();
            if (i < index - 1) inAddress += "|";
        }
        //组装outputs
        List<Transaction.Output> outputs = new ArrayList<>();
        String outAddress = "";
        outputs.add(new Transaction.Output(sendAddress, sendAmount));
        outAddress += sendAddress;
        if (!isSendAll) {
            // TODO: 2017/12/19   找零逻辑优化
            long moreAmount = amount - fee - sendAmount;
            if (moreAmount > 0) {
                String newAddress = getMvpView().getNewAddress();
                outputs.add(new Transaction.Output(newAddress, moreAmount));
                outAddress += ("|" + newAddress);
            }

        }
        //js调用构建交易
        Transaction transaction = new Transaction(inputs, outputs);
        String txJson = JsonUtils.serialize(transaction);
        Log.d(TAG, "buildTransaction() called with :seedHex=[" + transaction + "], jsResult = [" + txJson + "], fee = [" + fee + "], feeByte = [" + feeByte + "]");
        String finalInAddress = inAddress;
        String finalOutAddress = outAddress;

        BitcoinJsWrapper.getInstance().buildTransaction(seedHex, txJson, new BitcoinJsWrapper.Callback() {
            @Override
            public void call(String key, String jsResult) {

                if (!isViewAttached()) {
                    return;
                }
                if (StringUtils.isEmpty(jsResult)) {
                    getMvpView().sendTransactionFail(null);
                    return;
                }
                JsResult result = null;
                try {
                    result = JsonUtils.deserialize(jsResult, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (result == null) {
                    getMvpView().sendTransactionFail(null);
                    return;
                }
                if (result.status == JsResult.STATUS_SUCCESS) {
                    String[] data = result.getData();
                    if (data != null && data.length > 0) {
                        String txHex = data[0];
                        Log.d(TAG, "buildTransaction() called with: key = [" + key + "], jsResult = [" + jsResult + "]");
                        byte[] hashData = EncryptUtils.encryptSHA256(EncryptUtils.encryptSHA256(ConvertUtils.hexString2Bytes(txHex)));
                        //反转字节数组
                        ConvertUtils.reverse(hashData);
                        String txHash = ConvertUtils.bytes2HexString(hashData);
                        Log.d(TAG, "buildTransaction() called with: reverseHex = [" + txHash + "]");
                        sendTransaction(txHash, txHex, finalInAddress, finalOutAddress);
                    } else {
                        getMvpView().sendTransactionFail(null);
                    }

                } else {
                    getMvpView().sendTransactionFail(result.getMessage());
                }
            }
        });

    }

    @Override
    public void sendTransaction(String txHash, String txHex, String inAddress, String outAddress) {


        if (!isValidWallet() || !isValidXPublicKey() || !isValidBtcAddress()) return;

        Wallet wallet = getMvpView().getWallet();
        String extendedKeysHash = EncryptUtils.encryptMD5ToString(wallet.getExtentedPublicKey());
        long outAmount = getMvpView().getSendAmount();

        getCompositeDisposable().add(getModelManager()
                .sendTransaction(new SendTransactionRequest(extendedKeysHash, inAddress, outAddress, outAmount, txHash, txHex, getMvpView().getRemark()))
                .compose(this.applyScheduler())
                .subscribeWith(new BaseSubcriber<ApiResponse<SendTransactionResponse>>() {
                    @Override
                    public void onNext(ApiResponse<SendTransactionResponse> sendTransactionResponseApiResponse) {
                        super.onNext(sendTransactionResponseApiResponse);
                        if (!isViewAttached()) {
                            return;
                        }
                        if (sendTransactionResponseApiResponse != null && sendTransactionResponseApiResponse.isSuccess()) {
                            getMvpView().sendTransactionSuccess(txHash);
                        } else {
                            getMvpView().sendTransactionFail(sendTransactionResponseApiResponse.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (!isViewAttached()) {
                            return;
                        }
                        // handle error here
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            handleApiError(anError);
                        }
                    }
                }));
    }

    @Override
    public void computeFee() {
        List<GetTxElementResponse.UtxoBean> unspentList = new ArrayList<>();
        unspentList.clear();
        unspentList.addAll(getMvpView().getUnspentList());
        //计算手续费 余额从小大排列
        Collections.sort(unspentList, (o1, o2) -> {
            //倒序排列
            return o2.getSumOutAmount() - o1.getSumOutAmount();
        });

        long feeByte = getMvpView().getFeeByte();
        boolean isSendAll = getMvpView().isSendAll();
        long sendAmount = getMvpView().getSendAmount();
        Wallet wallet = getMvpView().getWallet();

        long amount = 0;
        int index = -1;
        long fee = 0;
        for (int i = 0; i < unspentList.size(); i++) {
            GetTxElementResponse.UtxoBean unspent = unspentList.get(i);
            fee = (CryptoConstants.INPUT_SIZE * (i + 1) + CryptoConstants.OUTPUT_SIZE * (isSendAll ? 1 : 2)) * feeByte;
            amount += unspent.getSumOutAmount();
            if (!isSendAll && amount - fee >= sendAmount) {
                index = i;
                break;
            }
        }
        if (isSendAll) {
            index = unspentList.size() - 1;
            //通过utxo设置余额
            wallet.setBalance(amount);
        }
        //check amount
        if (amount <= 0 || index == -1 || fee <= 0) {
            getMvpView().amountNoEnough();
            return;
        }
        //check mini fee
        if (fee < CryptoConstants.BTC_MIN_FEE) {
            fee = CryptoConstants.BTC_MIN_FEE;
        }
        //check send amount
        if (amount - fee <= 0) {
            getMvpView().amountNoEnough();
            return;
        }
        getMvpView().compteFee(fee);

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

    private boolean isValidWallet() {
        if (getMvpView().getWallet() == null) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

    public boolean isValidXPublicKey() {
        if (StringUtils.isEmpty(getMvpView().getWallet().getExtentedPublicKey())) {
            getMvpView().getWalletFail();
            return false;
        }
        return true;
    }

    private boolean isValidBtcAddress() {
        if (StringUtils.isEmpty(getMvpView().getSendAddress())) {
            getMvpView().sendTransactionFail(null);
            return false;
        }
        return true;
    }

    public boolean isValidUnspendList() {
        if (StringUtils.isEmpty(getMvpView().getUnspentList())) {
            getMvpView().getTxElementFail();
            return false;
        }
        return true;
    }
}
