package com.jianyuyouhun.mobile.okrequester.library.requester;

import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;
import com.jianyuyouhun.mobile.okrequester.library.requester.parser.StringResponseParser;

/**
 * 请求结果默认解析成string
 */
public abstract class BaseStringRequester extends BaseRequester<String> {

    public BaseStringRequester(@NonNull OnHttpResultListener<String> listener) {
        super(new StringResponseParser(), listener);
    }
}
