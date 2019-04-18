package com.jianyuyouhun.mobile.okrequester.library.requester.creator;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 请求参数解析器
 * Created by wangyu on 2018/5/2.
 */

public interface BodyCreatorAction {

    RequestBody onCreate(Map<String, Object> params);
}
