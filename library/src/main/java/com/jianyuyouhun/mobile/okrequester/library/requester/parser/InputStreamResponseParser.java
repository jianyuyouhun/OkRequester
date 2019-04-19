package com.jianyuyouhun.mobile.okrequester.library.requester.parser;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;
import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;

import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 将http结果处理为stream
 */
@SuppressWarnings("unused")
public class InputStreamResponseParser implements ResponseParser<InputStream> {

    private Handler handler = HttpHolder.getInstance().getHandler();

    @Override
    public void onParseResponse(@NonNull Response response, OnHttpResultListener<InputStream> listener) {
        try {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                InputStream result = null;
                if (body != null) {
                    result = body.byteStream();
                }
                final InputStream finalResult = result;
                handler.post(() -> listener.onResult(response.code(), finalResult, response.message()));
            } else {
                handler.post(() -> listener.onResult(response.code(), null, response.message()));
            }
        } catch (Exception e) {
            onError(e, listener);
        }
    }

    @Override
    public void onError(final Exception e, final OnHttpResultListener<InputStream> listener) {
        handler.post(() -> listener.onError(e));
    }
}
