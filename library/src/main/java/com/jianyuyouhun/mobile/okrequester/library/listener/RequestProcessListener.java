package com.jianyuyouhun.mobile.okrequester.library.listener;

import java.util.Map;

/**
 * 请求过程监听
 * Created by wangyu on 2018/6/9.
 */

public interface RequestProcessListener {
    void onPreRequest(String url, String route, Map<String, Object> params);
    void onResult(String content);
    void onError(String url, String route, Exception e);
}
