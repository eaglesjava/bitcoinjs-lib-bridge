package com.bitbill.www.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitcoinJsWrapper {
    private WebView mWebView;
    private JsInterface mJsInterface;

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
    }

    private void callMnemonicRandom() {
        mWebView.loadUrl("javascript:getMnemonicRandom()");
    }

    public void getMnemonic(JsInterface.Callback callback) {
        mJsInterface.addCallBack(JsInterface.GET_MNEMONIC, callback);
        callMnemonicRandom();
    }

    //静态内部类确保了在首次调用getInstance()的时候才会初始化SingletonHolder，从而导致实例被创建。
    //并且由JVM保证了线程的安全。
    private static class SingletonHolder {
        private static final BitcoinJsWrapper instance = new BitcoinJsWrapper();
    }

    public static final class JsInterface {
        public static final String GET_MNEMONIC = "getMnemonic";
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
        public void getMnemonic(final String mnemonic) {
            callAndBack(GET_MNEMONIC, mnemonic);
        }

        private void callAndBack(String key, String result) {
            ((Callback) mCallBackMap.get(key)).call(key, result);
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
