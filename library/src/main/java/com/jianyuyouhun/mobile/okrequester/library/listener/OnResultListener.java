package com.jianyuyouhun.mobile.okrequester.library.listener;

/**
 *
 * Created by wangyu on 2018/6/11.
 */

public interface OnResultListener<T> extends ErrorCode {
    void onResult(int code, T t, String msg);
}
