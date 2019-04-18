package com.jianyuyouhun.mobile.okrequester.library.listener;

/**
 * 通用异步回调
 * Created by wangyu on 2018/6/11.
 */

public interface OnResultListener<T> {
    void onResult(int code, T t, String msg);
}
