package com.bitbill.www.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bitbill.www.R;

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

}
