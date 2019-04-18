package com.jianyuyouhun.mobile.okrequester.library.requester.parser;

import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;

import okhttp3.Response;

/**
 * 请求结果处理
 */
public interface ResponseParser<In> {
    /**
     * 请求成功后这里并没有指定response将要转换成什么类型的数据，所以请求成功的数据并不会打印日志，打印逻辑在实现类如{@link StringResponseParser}中完成
     */
    void onParseResponse(@NonNull Response response, OnHttpResultListener<In> listener);

    void onError(Exception e, OnHttpResultListener<In> listener);
}
