package com.jianyuyouhun.mobile.okrequester.library.listener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 流文件转bitmap，可在此进行图片的品质修改等操作
 */
public abstract class OnBitmapResultListener implements OnHttpResultListener<InputStream> {

    @Override
    public void onResult(int code, InputStream inputStream, String msg) {
        Bitmap bitmap = null;
        if (code == HTTP_OK) {
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        onResultData(code, bitmap, msg);
    }

    abstract void onResultData(int code, Bitmap bitmap, String msg);
}
