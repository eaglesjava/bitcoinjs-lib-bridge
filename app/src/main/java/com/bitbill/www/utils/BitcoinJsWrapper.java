package com.bitbill.www.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by zhuyuanbao on 2017/11/7.
 */
public class BitcoinJsWrapper {
    private WebView mWebView;
    private String mBitcoinKey;
    private String mResut;

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

        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "android");

        mWebView.loadUrl("file:///android_asset/bitcoin/index_android.html");
    }

    private void callGetBitcoinKey() {

        mWebView.loadUrl("javascript:getBitcoinKey()");
    }

    public String getBitcoinKey() {
        callGetBitcoinKey();
        return mBitcoinKey;
    }

    public BitcoinJsWrapper setBitcoinKey(String mBitcoinKey) {
        this.mBitcoinKey = mBitcoinKey;
        return this;
    }

    private void callTestParams(int num) {

        mWebView.loadUrl("javascript:testParams(" + num + ")");
    }

    public BitcoinJsWrapper setTestParams(String mResut) {
        this.mResut = mResut;
        return this;
    }

    public String getTestParams(int num) {
        callTestParams(num);
        return mResut;
    }

    //静态内部类确保了在首次调用getInstance()的时候才会初始化SingletonHolder，从而导致实例被创建。
    //并且由JVM保证了线程的安全。
    private static class SingletonHolder {
        private static final BitcoinJsWrapper instance = new BitcoinJsWrapper();
    }

    static final class JavaScriptInterface {

        private final BitcoinJsWrapper mBitcoinJsWrapper;

        JavaScriptInterface(BitcoinJsWrapper bitcoinJsWrapper) {
            mBitcoinJsWrapper = bitcoinJsWrapper;
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void getBitcoinKey(final String key) {
            mBitcoinJsWrapper.setBitcoinKey(key);
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void getResult(final String key) {
            mBitcoinJsWrapper.setTestParams(key);
        }
    }
}
