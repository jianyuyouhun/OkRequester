package com.jianyuyouhun.mobile.okrequester.library.listener;


import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;
import com.jianyuyouhun.mobile.okrequester.library.Logger;

import java.util.Locale;
import java.util.Map;

/**
 * DefaultProcessListener
 *
 * @author wangyu
 * @date 2018/6/10
 */

public class DefaultProcessListener implements RequestProcessListener {
    private static final String TAG = "WEB_LOG";

    @Override
    public void onPreRequest(String url, String route, Map<String, Object> params) {
        if (HttpHolder.isDebug) {
            String string = String.format(Locale.getDefault(), "request, url = %s", url + "?" + logParams(params));
            Logger.i(TAG, string);
        }
    }

    @Override
    public void onResult(int code, String url, String route, String content) {
        if (HttpHolder.isDebug) {
            String serverContent = content == null ? "null" : content.replace("%", "%%");
            String string = String.format(Locale.getDefault(), "response, url = %s, code = %d, content = %s", url, code, serverContent);
            Logger.i(TAG, string);
        }
    }

    @Override
    public void onError(String url, String route, Exception e) {
        //调试模式下打印web请求日志
        if (HttpHolder.isDebug) {
            String string = String.format(Locale.getDefault(), "response, url = %s, error = %s", url, e);
            Logger.i(TAG, string);
        }
    }

    /**
     * 获取要打印的请求参数
     *
     * @param params  表单
     * @return 拼接后的参数字符串
     */
    private String logParams(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet()) {
            builder.append(key)
                    .append("=")
                    .append(params.get(key))
                    .append("&");
        }
        return builder.toString();
    }
}
