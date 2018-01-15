package com.bitbill.www.common.app;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.provider.MediaStore;

import com.bitbill.www.common.utils.ScreenUtils;

import java.io.File;

public class ScreenshotContentObserver extends ContentObserver {

    private Context mContext;
    private int imageNum;

    private OnScreenShotChangeListener mOnScreenShotChangeListener;

    public ScreenshotContentObserver(Context context) {
        super(null);
        mContext = context;
    }

    public void startObserve() {
        register();
    }

    public void stopObserve() {
        unregister();
    }

    private void register() {
        mContext.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, this);
    }

    private void unregister() {
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        String[] columns = {
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.DATA,
        };
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.MediaColumns.DATE_MODIFIED + " desc");
            if (cursor == null) {
                return;
            }
            int count = cursor.getCount();
            if (imageNum == 0) {
                imageNum = count;
            } else if (imageNum >= count) {
                return;
            }
            imageNum = count;
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                long addTime = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
                if (matchAddTime(addTime) && matchPath(filePath) && matchSize(filePath)) {
                    doReport(filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加时间与当前时间不超过1.5s,大部分时候不超过1s。
     *
     * @param addTime 图片添加时间，单位:秒
     */
    private boolean matchAddTime(long addTime) {
        return System.currentTimeMillis() - addTime * 1000 < 60 * 1000;
    }

    /**
     * 尺寸不大于屏幕尺寸（发现360奇酷手机可以对截屏进行裁剪）
     */
    private boolean matchSize(String filePath) {
        Point size = ScreenUtils.getScreenMetrics(mContext);//获取屏幕尺寸

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        return size.x >= options.outWidth && size.y >= options.outHeight;
    }


    /**
     * 已调查的手机截屏图片的路径中带有screenshot
     */
    private boolean matchPath(String filePath) {
        String lower = filePath.toLowerCase();
        return lower.contains("screenshot");
    }

    private void doReport(String filePath) {
        //截屏
        File file = new File(filePath);

        // 通知截图风险
        if (mOnScreenShotChangeListener != null) {
            mOnScreenShotChangeListener.onScreenShot(file);
        }
    }

    public void setOnScreenShotChangeListener(OnScreenShotChangeListener onScreenShotChangeListener) {
        mOnScreenShotChangeListener = onScreenShotChangeListener;
    }

    public interface OnScreenShotChangeListener {
        void onScreenShot(File shotFile);
    }
}