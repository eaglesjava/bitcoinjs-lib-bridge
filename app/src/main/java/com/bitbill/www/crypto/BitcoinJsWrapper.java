package com.bitbill.www.crypto;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitcoinJsWrapper {
    public static final String MNEMONIC_TO_SEED_HEX = "mnemonicToSeedHex";
    public static final String GENERATE_MNEMONIC_RANDOM_CN = "generateMnemonicRandomCN";
    public static final String GET_BITCOIN_ADDRESS_BY_SEED_HEX = "getBitcoinAddressBySeedHex";
    public static final String GET_BITCOIN_ADDRESS_BY_MASTER_XPUBLIC_KEY = "getBitcoinAddressByMasterXPublicKey";
    public static final String GET_BITCOIN_MASTER_XPUBLIC_KEY = "getBitcoinMasterXPublicKey";
    private WebView mWebView;
    private JsInterface mJsInterface;
    private Handler mHandler;

    private BitcoinJsWrapper() {
    }

    public static BitcoinJsWrapper getInstance() {
        return SingletonHolder.instance;
    }

    public void init(Context context) {
        mWebView = new WebView(context);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        // 限制在WebView中打开网页，而不用默认浏览器
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.setWebChromeClient(new MyWebChromeClient());

        mJsInterface = new JsInterface(this);
        mWebView.addJavascriptInterface(mJsInterface, "android");

        mWebView.loadUrl("file:///android_asset/bitcoin/index_android.html");

        mHandler = new Handler();
    }

    private void executeJS(final String js) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(js);
            }
        });
    }

    /**
     * 随机产生中文助记词
     *
     * @param callback
     */
    public void generateMnemonicRandomCN(JsInterface.Callback callback) {
        mJsInterface.addCallBack(GENERATE_MNEMONIC_RANDOM_CN, callback);
        executeJS("javascript:generateMnemonicRandomCN()");
    }

    /**
     * 助记词生成seed
     * @param nemonic 助记词字符串，以空格隔开
     * @param pwd
     * @param callback 密码
     */
    public void mnemonicToSeedHex(String nemonic, String pwd, JsInterface.Callback callback) {
        mJsInterface.addCallBack(MNEMONIC_TO_SEED_HEX, callback);
        executeJS("javascript:mnemonicToSeedHex('" + nemonic + "','" + pwd + "')");
    }

    /**
     * 根据seed生成指定index的地址
     *
     * @param seedHex  seed的十六进制字符串
     * @param index
     * @param callback
     */
    public void getBitcoinAddressBySeedHex(String seedHex, int index, JsInterface.Callback callback) {
        mJsInterface.addCallBack(GET_BITCOIN_ADDRESS_BY_SEED_HEX, callback);
        executeJS("javascript:getBitcoinAddressBySeedHex('" + seedHex + "'," + index + ")");
    }

    public void getBitcoinAddressByMasterXPublicKey(String xpub, int index, JsInterface.Callback callback) {
        mJsInterface.addCallBack(GET_BITCOIN_ADDRESS_BY_MASTER_XPUBLIC_KEY, callback);
        executeJS("javascript:getBitcoinAddressByMasterXPublicKey('" + xpub + "'," + index + ")");
    }

    public void getBitcoinMasterXPublicKey(String seedHex, JsInterface.Callback callback) {
        mJsInterface.addCallBack(GET_BITCOIN_MASTER_XPUBLIC_KEY, callback);
        executeJS("javascript:getBitcoinMasterXPublicKey('" + seedHex + "')");
    }

    //静态内部类确保了在首次调用getInstance()的时候才会初始化SingletonHolder，从而导致实例被创建。
    //并且由JVM保证了线程的安全。
    private static class SingletonHolder {
        private static final BitcoinJsWrapper instance = new BitcoinJsWrapper();
    }

    public static final class JsInterface {
        private final BitcoinJsWrapper mBitcoinJsWrapper;
        private Map mCallBackMap;

        JsInterface(BitcoinJsWrapper bitcoinJsWrapper) {
            mBitcoinJsWrapper = bitcoinJsWrapper;
            mCallBackMap = new HashMap();
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void generateMnemonicRandomCN(final String mnemonic) {
            callAndBack(GENERATE_MNEMONIC_RANDOM_CN, mnemonic);
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void mnemonicToSeedHex(final String seedHex) {
            callAndBack(MNEMONIC_TO_SEED_HEX, seedHex);
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void getBitcoinAddressBySeedHex(final String address) {
            callAndBack(GET_BITCOIN_ADDRESS_BY_SEED_HEX, address);
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void getBitcoinAddressByMasterXPublicKey(final String address) {
            callAndBack(GET_BITCOIN_ADDRESS_BY_MASTER_XPUBLIC_KEY, address);
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void getBitcoinMasterXPublicKey(final String address) {
            callAndBack(GET_BITCOIN_MASTER_XPUBLIC_KEY, address);
        }

        private void callAndBack(String key, String result) {
            Callback callback = (Callback) mCallBackMap.get(key);
            if (callback != null) {
                callback.call(key, result);
            }
            mCallBackMap.remove(key);
        }

        public void addCallBack(String key, Callback callback) {
            mCallBackMap.put(key, callback);
        }

        public interface Callback {
            void call(String key, String jsResult);
        }
    }

}
