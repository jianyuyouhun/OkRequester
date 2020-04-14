package com.jianyuyouhun.mobile.okrequester.library.requester;



import com.jianyuyouhun.mobile.okrequester.library.listener.OnResultListener;

import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * json格式请求基类
 *
 * @author wangyu
 * @date 2018/6/25
 */

public abstract class BaseJsonRequester<T> extends BaseRequester<T, JSONObject> {

    public BaseJsonRequester(@NonNull OnResultListener<T> listener) {
        super(listener);
    }

    @Override
    protected int parseCode(@NonNull JSONObject jsonObject) {
        return jsonObject.optInt("code");
    }

    @Override
    protected String parseMessage(@NonNull JSONObject jsonObject) {
        return jsonObject.optString("msg");
    }

    @Override
    protected JSONObject parseIn(@NonNull String content) throws Exception {
        return new JSONObject(content);
    }
}
