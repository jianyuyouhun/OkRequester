package com.jianyuyouhun.mobile.okrequester.library.requester.parser;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;
import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;
import com.jianyuyouhun.mobile.okrequester.library.listener.RequestProcessListener;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 将http结果处理成字符串
 */
@SuppressWarnings("unused")
public class StringResponseParser implements ResponseParser<String> {

    private HttpHolder httpHolder = HttpHolder.getInstance();
    private Handler handler = HttpHolder.getInstance().getHandler();

    @Override
    public void onParseResponse(@NonNull Response response, OnHttpResultListener<String> listener) {
        try {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                String result = null;
                if (body != null) {
                    result = body.string();
                }
                String finalResult = result;
                if (HttpHolder.isDebug) {//打印日志
                    for (RequestProcessListener processListener : httpHolder.getRequestProcessListeners()) {
                        processListener.onResult(finalResult);
                    }
                }
                handler.post(() -> listener.onResult(response.code(), finalResult, response.message()));
            } else {
                handler.post(() -> listener.onResult(response.code(), null, response.message()));
            }
        } catch (Exception e) {
            onError(e, listener);
        }

    }

    @Override
    public void onError(Exception e, OnHttpResultListener<String> listener) {
        handler.post(() -> listener.onError(e));
    }
}
