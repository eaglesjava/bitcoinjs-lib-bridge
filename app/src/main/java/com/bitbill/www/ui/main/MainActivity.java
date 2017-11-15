package com.bitbill.www.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.utils.BitcoinJsWrapper;

/**
 * Created by isanwenyu@163.com on 2017/11/13.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);

        mTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitcoinJsWrapper.getInstance().generateMnemonicRandomCN((key, jsResult) -> {
                    Log.d(TAG, "generateMnemonicRandomCN.call() called with: key = [" + key + "], jsResult = [" + jsResult + "]");
                    BitcoinJsWrapper.getInstance().mnemonicToSeedHex(jsResult, "123456", (key1, jsResult1) -> {
                        Log.d(TAG, "mnemonicToSeedHex.call() called with: key = [" + key1 + "], jsResult = [" + jsResult1 + "]");
                        BitcoinJsWrapper.getInstance().getBitcoinAddressBySeedHex(jsResult1, 0, (key2, jsResult2) -> {
                            Log.d(TAG, "getBitcoinAddressBySeedHex.call() called with: key = [" + key2 + "], jsResult = [" + jsResult2 + "]");
                        });
                        BitcoinJsWrapper.getInstance().getBitcoinMasterXPublicKey(jsResult1, (key3, jsResult3) -> {
                            Log.d(TAG, "getBitcoinMasterXPublicKey.call() called with: key = [" + key3 + "], jsResult = [" + jsResult3 + "]");
                            BitcoinJsWrapper.getInstance().getBitcoinAddressByMasterXPublicKey(jsResult3, 0, (key4, jsResult4) -> {
                                Log.d(TAG, "getBitcoinAddressByMasterXPublicKey.call() called with: key = [" + key4 + "], jsResult = [" + jsResult4 + "]");
                                setResult(jsResult4);
                            });
                        });
                    });
                });
            }
        });

    }

    private void setResult(final String jsResult) {
        runOnUiThread(() -> mTextMessage.setText(jsResult));
    }

}
