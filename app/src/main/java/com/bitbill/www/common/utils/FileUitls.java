package com.bitbill.www.common.utils;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.bitbill.www.app.AppConstants;

import java.io.File;
import java.util.Locale;

/**
 * Created by isanwenyu on 2018/1/31.
 */

public class FileUitls {

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath() + File.separator + AppConstants.SCHEME_BITBILL;
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + AppConstants.SCHEME_BITBILL;
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    public static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2f", bytes / (1024.00 * 1024.00));
    }

}
