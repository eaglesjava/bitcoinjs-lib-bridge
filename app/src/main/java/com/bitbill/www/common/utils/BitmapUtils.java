/*
 * Copyright (c) 2017 bitbill.com
 */

package com.bitbill.www.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by isanwenyu@163.com on 2017/9/13.
 */
public class BitmapUtils {

    public static void saveBitmapToFile(Bitmap bitmap, String filePath, int quality) {
        File file = new File(filePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int quality, int inSampleSize) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            Bitmap compressedBitmap = BitmapFactory.decodeStream(bis, null, options);
            bos.close();
            bis.close();
            return compressedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeBitmapFromFile(String filePath, int inSampleSize) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(filePath, options);

    }
}
