package com.bitbill.www.crypto;

import android.content.Context;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bitbill.www.common.utils.JsonUtils;
import com.google.gson.JsonSyntaxException;


/**
 * Created by isanwenyu@163.com on 2017/11/7.
 */
public class BitcoinJsWrapper {
    private static final String TAG = "BitcoinJsWrapper";
    private static WebView mWebView;

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
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                // TODO Auto-generated method stub
                if (consoleMessage.message().matches("Uncaught \\w*Error")) {
                    // do something...
                    Log.d(TAG, "onConsoleMessage() called with: consoleMessage = [" + consoleMessage + "]");
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        mWebView.loadUrl("file:///android_asset/bitcoin/index_android.html");
    }

    private void executeJS(final String jsFunction, Callback callback) {
        if (jsFunction == null) {
            throw new NullPointerException("jsFuction is null");
        }
        String excuteJs = "javascript:" + jsFunction;
        String functionName = jsFunction.substring(0, jsFunction.indexOf("("));
        Log.d(TAG, "executeJS() called with: jsFunction = [" + jsFunction + "], callback = [" + callback + "]");
        mWebView.evaluateJavascript(jsFunction, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.d(TAG, "onReceiveValue() called with: value = [" + value + "]");
                if (callback != null) {
                    String[] values = new String[0];
                    try {
                        values = JsonUtils.deserialize(value, String[].class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    callback.call(functionName, values);
                }

            }
        });

    }

    public void generateMnemonicCNRetrunSeedHexAndXPublicKey(Callback callback) {
        executeJS("generateMnemonicCNRetrunSeedHexAndXPublicKey()", callback);
    }

    public void validateMnemonicReturnSeedHexAndXPublicKey(String nemonic, Callback callback) {
        executeJS("validateMnemonicReturnSeedHexAndXPublicKey('" + nemonic + "')", callback);
    }

    public void getBitcoinAddressByMasterXPublicKey(String xpub, long index, Callback callback) {
        executeJS("getBitcoinAddressByMasterXPublicKey('" + xpub + "'," + index + ")", callback);
    }

    public interface Callback {
        void call(String key, String... jsResult);
    }

    //静态内部类确保了在首次调用getInstance()的时候才会初始化SingletonHolder，从而导致实例被创建。
    //并且由JVM保证了线程的安全。
    private static class SingletonHolder {
        private static final BitcoinJsWrapper instance = new BitcoinJsWrapper();
    }

}
