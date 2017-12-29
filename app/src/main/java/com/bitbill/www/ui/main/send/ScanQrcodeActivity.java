package com.bitbill.www.ui.main.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.common.base.presenter.MvpPresenter;
import com.bitbill.www.common.base.view.BaseFragment;
import com.bitbill.www.common.base.view.BaseToolbarActivity;
import com.bitbill.www.common.utils.StringUtils;
import com.bitbill.www.ui.main.contact.SearchContactResultActivity;

import java.util.Map;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ScanQrcodeActivity extends BaseToolbarActivity implements QRCodeView.Delegate {

    public static final int REQUEST_CODE_SCAN_QRCODE = 111;
    public static final int RESULT_CODE_SCAN_QRCODE_SUCCESS = 333;
    public static final String EXTRA_SCAN_QRCODE_RESULT = "scan_qrcode_result";
    private static final String TAG = "ScanQrcodeActivity";
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    public static void start(Context context) {
        Intent starter = new Intent(context, ScanQrcodeActivity.class);
        context.startActivity(starter);
    }

    public static void startForResult(BaseFragment fragment) {
        Intent starter = new Intent(fragment.getBaseActivity(), ScanQrcodeActivity.class);
        fragment.startActivityForResult(starter, REQUEST_CODE_SCAN_QRCODE);
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
        Intent data = new Intent();
        Map<String, String> resultMap = StringUtils.parseScanResult(result);
        if (StringUtils.isEmpty(resultMap)) {
            showMessage("解析扫码地址失败");
            return;
        }
        String address = resultMap.get(AppConstants.QUERY_ADDRESS);
        String amount = resultMap.get(AppConstants.QUERY_AMOUNT);
        String contactId = resultMap.get(AppConstants.QUERY_ID);

        if (StringUtils.isNotEmpty(contactId)) {
            //跳转到联系人搜索结果页面
            SearchContactResultActivity.start(ScanQrcodeActivity.this, null, contactId);
        } else if (StringUtils.isNotEmpty(amount)) {
            SendAmountActivity.start(ScanQrcodeActivity.this, address, amount);
        } else {
            data.putExtra(EXTRA_SCAN_QRCODE_RESULT, address);
            setResult(RESULT_CODE_SCAN_QRCODE_SUCCESS, data);
        }
        finish();
    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        showMessage("相机打开失败，请检查权限");
    }
}
