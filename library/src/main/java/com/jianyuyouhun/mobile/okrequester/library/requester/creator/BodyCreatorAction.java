package com.jianyuyouhun.mobile.okrequester.library.requester.creator;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 请求参数解析器
 *
 * @author wangyu
 * @date 2018/5/2
 */

public interface BodyCreatorAction {
    /**
     * 创建请求体
     * @param params    参数
     * @return  请求数据包
     */
    RequestBody onCreate(Map<String, Object> params);
}
