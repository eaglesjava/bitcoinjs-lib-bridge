package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.common.utils.UIHelper;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ScanQrcodeActivity extends BaseToolbarActivity implements QRCodeView.Delegate {

    private static final String TAG = "ScanQrcodeActivity";
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;
    private String mFromTag;

    public static void start(Context context, String fromTag) {
        Intent starter = new Intent(context, ScanQrcodeActivity.class);
        starter.putExtra(AppConstants.EXTRA_FROM_TAG, fromTag);
        context.startActivity(starter);
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mFromTag = getIntent().getStringExtra(AppConstants.EXTRA_FROM_TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public MvpPresenter getMvpPresenter() {
        return null;
    }

    @Override
    public void injectComponent() {

    }

    @Override
    public void onBeforeSetContentLayout() {
        setTitle(R.string.title_activity_scan_qrcode);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        mQRCodeView.setDelegate(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_qrcode;
    }

    /**
     * 处理扫描结果
     *
     * @param result
     */
    @Override
    public void onScanQRCodeSuccess(String result) {

        Log.i(TAG, "result:" + result);
        vibrate();
        mQRCodeView.startSpot();
        if (StringUtils.isEmpty(result)) {
            showMessage(R.string.fail_parse_scan_result);
            return;
        }
        UIHelper.parseScanResult(ScanQrcodeActivity.this, result, mFromTag);
        finish();
    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        showMessage(R.string.fail_open_camera);
    }
}
