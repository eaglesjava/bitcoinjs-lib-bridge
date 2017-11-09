package com.bitbill.www.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bitbill.www.R;
import com.bitbill.www.utils.BitcoinJsWrapper;

import io.github.isanwenyu.cardview.CardViewCompat;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        CardViewCompat one = (CardViewCompat) findViewById(R.id.cv_one);
        CardViewCompat two = (CardViewCompat) findViewById(R.id.cv_two);

        mTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitcoinJsWrapper.getInstance().getMnemonic(new BitcoinJsWrapper.JsInterface.Callback() {
                    @Override
                    public void call(String key, final String jsResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextMessage.setText(jsResult);
                            }
                        });
                    }
                });
            }
        });

    }

}
