package com.jianyuyouhun.mobile.okrequester.library.listener;

import java.util.Map;

/**
 * 请求过程监听
 *
 * @author wangyu
 * @date 2018/6/9
 */

public interface RequestProcessListener {

    /**
     * 预处理，比如请求之前打印请求参数
     * @param url       url
     * @param route     路由
     * @param params    参数
     */
    void onPreRequest(String url, String route, Map<String, Object> params);

    /**
     * 结果处理
     * @param code      code
     * @param url       url
     * @param route     路由
     * @param content   返回内容
     */
    void onResult(int code, String url, String route, String content);

    /**
     * 异常处理
     * @param url       url
     * @param route     路由
     * @param e         异常信息
     */
    void onError(String url, String route, Exception e);
}
