package com.jianyuyouhun.mobile.okrequester.library.requester;

import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;
import com.jianyuyouhun.mobile.okrequester.library.requester.parser.InputStreamResponseParser;

import java.io.InputStream;

public abstract class BaseStreamRequester extends BaseRequester<InputStream> {

    public BaseStreamRequester(@NonNull OnHttpResultListener<InputStream> listener) {
        super(new InputStreamResponseParser(), listener);
    }
}
