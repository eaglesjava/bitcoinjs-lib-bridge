package com.bitbill.www.ui.wallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bitbill.www.R;
import com.bitbill.www.ui.widget.EditTextWapper;

/**
 * 导入钱包
 * Created by isanwenyu@163.com on 2017/11/14.
 */
public class CreateOrImportWalletActivity extends AppCompatActivity {


    // UI references.
    private EditTextWapper mWalletNameView;
    private EditTextWapper mPasswordView;
    private View mProgressView;
    private View mWalletFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_import_wallet);
        // Set up the login form.
        mWalletNameView = (EditTextWapper) findViewById(R.id.etw_wallet_name);

        mPasswordView = (EditTextWapper) findViewById(R.id.etw_trade_pwd);

        Button mStartBtn = (Button) findViewById(R.id.btn_start);
        mStartBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateOrImportWallet();
            }
        });

    }

    private void attemptCreateOrImportWallet() {

        // Reset errors.
        mWalletNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the create or import wallet attempt.
        String name = mWalletNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid wallet name.
        if (TextUtils.isEmpty(name)) {
            mWalletNameView.setError(getString(R.string.error_wallet_name_required));
            focusView = mWalletNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt create or import and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the wallet create or import attempt.
            showProgress(true);
            // TODO: 2017/11/14 create or import wallet logic
        }
    }

    private void showProgress(boolean show) {

    }

    private boolean isWalletNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6 && password.length() < 20;
    }


}

