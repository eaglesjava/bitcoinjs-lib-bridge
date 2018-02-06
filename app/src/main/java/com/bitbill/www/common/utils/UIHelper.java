package com.bitbill.www.common.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.bitbill.www.BuildConfig;
import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.main.contact.AddBtcContactByAddressFragment;
import com.bitbill.www.ui.main.contact.AddContactByAddressActivity;
import com.bitbill.www.ui.main.contact.ContactFragment;
import com.bitbill.www.ui.main.contact.SearchContactResultActivity;
import com.bitbill.www.ui.main.send.ScanResultActivity;

import java.io.File;

import static com.bitbill.www.common.utils.StringUtils.isEmpty;
import static com.bitbill.www.common.utils.StringUtils.isNotEmpty;

/**
 * Created by isanwenyu on 2018/1/9.
 */

public class UIHelper {

    public static void openBrower(Context context, String url) {

        //从其他浏览器打开
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.text_chose_browser)));

    }

    /**
     * 复制粘贴文本
     *
     * @param content
     * @param context
     */
    public static void copy(Context context, String content) {
        if (isEmpty(content)) {
            return;
        }
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static void parseScanResult(Context context, String result, String fromTag) {
        if (isEmpty(result)) {
            return;
        }
        //解析协议
        if (result.toLowerCase().startsWith(AppConstants.SCHEME_BITCOIN) && result.contains(":")) {
            String address = null;
            String amount = null;
            int askIndex = result.indexOf("?");
            if (askIndex != -1 & askIndex + 1 < result.length()) {
                String amountString = result.substring(askIndex + 1);
                if (amountString.contains("amount=")) {
                    amount = amountString.replace("amount=", "");
                }
                address = result.substring(result.indexOf(":") + 1, askIndex);
            } else {
                address = result.substring(result.indexOf(":") + 1);
            }
            if (AddBtcContactByAddressFragment.TAG.equals(fromTag) || ContactFragment.TAG.equals(fromTag)) {
                AddContactByAddressActivity.start(context, address);
            } else if (StringUtils.isNotEmpty(amount)) {
                ScanResultActivity.start(context, address, amount);
            } else {
                MainActivity.start(context, null, address);
            }

        } else if (result.toLowerCase().startsWith(AppConstants.SCHEME_BITBILL)) {
            Uri parse = Uri.parse(result);
            if (AppConstants.HOST_BITBILL.equals(parse.getHost())) {
                String path = parse.getPath();
                if (isNotEmpty(path) && AppConstants.PATH_CONTACT.equals(path.replace("/", ""))) {
                    String contactId = parse.getQueryParameter(AppConstants.QUERY_ID);

                    //跳转到联系人搜索结果页面
                    SearchContactResultActivity.start(context, null, contactId);
                }
            }
        } else {
            if (AddBtcContactByAddressFragment.TAG.equals(fromTag) || ContactFragment.TAG.equals(fromTag)) {
                AddContactByAddressActivity.start(context, result);
            } else {
                MainActivity.start(context, null, result);
            }
        }
    }

    public static void sendEmail(Context context, String email) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse("mailto:" + email);
        intent.setData(content_url);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.text_chose_email)));
    }


    public static void installApk(Context context, File file) {

        if (context == null || file == null || !file.exists()) {
            return;
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            // 声明需要的零时权限
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 第二个参数，即第一步中配置的authorities
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }
}
