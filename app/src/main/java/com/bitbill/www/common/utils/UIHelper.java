package com.bitbill.www.common.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bitbill.www.R;
import com.bitbill.www.app.AppConstants;
import com.bitbill.www.ui.main.MainActivity;
import com.bitbill.www.ui.main.contact.AddContactByAddressActivity;
import com.bitbill.www.ui.main.contact.SearchContactResultActivity;
import com.bitbill.www.ui.main.send.SendAmountActivity;

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

    public static void parseScanResult(Context context, String result, boolean isFromSend) {
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

            SendAmountActivity.start(context, address, amount);

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
            if (isFromSend) {
                MainActivity.start(context, null, result);
            } else {
                AddContactByAddressActivity.start(context, result);
            }
        }
    }

}
