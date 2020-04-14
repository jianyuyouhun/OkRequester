package com.jianyuyouhun.mobile.okrequester.library.listener;

/**
 * OnResultListener
 *
 * @author wangyu
 * @date 2018/6/11
 */

public interface OnResultListener<T> extends ErrorCode {
    /**
     * 通用结果回调
     * @param code  code码
     * @param t     泛型数据
     * @param msg   msg
     */
    void onResult(int code, T t, String msg);
}
