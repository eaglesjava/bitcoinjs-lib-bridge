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
import com.bitbill.www.common.utils.StringUtils;
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
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                return true;
            }
        });
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
                    JsResult jsResult = null;
                    if (StringUtils.isNotEmpty(value)) {
                        value = value.substring(1, value.length() - 1).replace("\\", "");
                        try {
                            jsResult = JsonUtils.deserialize(value, JsResult.class);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.call(functionName, jsResult);

                }

            }
        });

    }

    public void generateMnemonicRetrunSeedHexAndXPublicKey(int isCN, Callback callback) {
        executeJS("generateMnemonicRetrunSeedHexAndXPublicKey(" + isCN + ")", callback);
    }

    public void validateMnemonicReturnSeedHexAndXPublicKey(String nemonic, Callback callback) {
        executeJS("validateMnemonicReturnSeedHexAndXPublicKey('" + nemonic + "')", callback);
    }

    public void getBitcoinAddressByMasterXPublicKey(String xpub, long index, Callback callback) {
        executeJS("getBitcoinAddressByMasterXPublicKey('" + xpub + "'," + index + ")", callback);
    }

    public void buildTransaction(String seedHex, String datas, Callback callback) {
        executeJS("buildTransaction('" + seedHex + "','" + datas + "')", callback);
    }

    public void validateAddress(String address, Callback callback) {
        executeJS("validateAddress('" + address + "')", callback);
    }

    public void getBitcoinContinuousAddressByMasterXPublicKey(String xpub, long fromIndex, long inIndex, Callback callback) {
        executeJS("getBitcoinContinuousAddressByMasterXPublicKey('" + xpub + "'," + fromIndex + "," + inIndex + ")", callback);
    }

    public interface Callback {
        void call(String key, JsResult result);
    }

    //静态内部类确保了在首次调用getInstance()的时候才会初始化SingletonHolder，从而导致实例被创建。
    //并且由JVM保证了线程的安全。
    private static class SingletonHolder {
        private static final BitcoinJsWrapper instance = new BitcoinJsWrapper();
    }

}
