package com.bitbill.www.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by zhuyuanbao on 2017/11/7.
 */
public class BitcoinJsWrapper {
    private static WebView mWebView;

    public static void callGetBitcoinKey() {

        mWebView.loadUrl("javascript:getBitcoinKey()");
    }

    public static String getBitcoinKey(String key) {

        return key;
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
            BitcoinJsWrapper.getBitcoinKey(key);
        }
    }
}
