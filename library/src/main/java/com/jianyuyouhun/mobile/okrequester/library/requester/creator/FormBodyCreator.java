package com.jianyuyouhun.mobile.okrequester.library.requester.creator;

import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 表单参数解析
 *
 * @author wangyu
 * @date 2018/5/2
 */

public class FormBodyCreator implements BodyCreatorAction {
    @Override
    public RequestBody onCreate(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, Objects.requireNonNull(params.get(key)).toString());
        }
        return builder.build();
    }
}
