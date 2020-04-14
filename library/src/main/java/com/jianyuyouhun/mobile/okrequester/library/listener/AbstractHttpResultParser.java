package com.jianyuyouhun.mobile.okrequester.library.listener;

/**
 * 网络请求结果处理
 *
 * @author wangyu
 * @date 2018/3/6
 */

public abstract class AbstractHttpResultParser implements OnResultListener<String> {
    /**
     * 异常处理
     * @param e 异常信息
     */
    public abstract void onError(Exception e);
}
