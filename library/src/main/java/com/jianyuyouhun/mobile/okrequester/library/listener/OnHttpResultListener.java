package com.jianyuyouhun.mobile.okrequester.library.listener;

/**
 * 网络请求结果处理回调
 */
public interface OnHttpResultListener<In> extends OnResultListener<In> {
    void onError(Exception e);
}
