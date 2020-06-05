package com.jianyuyouhun.mobile.okrequester.library.requester.creator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 单层json格式参数解析
 * 由于本框架未依赖任何json2entity转换工具
 * 所以在拼接字段时仅考虑单层参数比如
 * {
 *     "id":"233",
 *     "name":"jianyuyouhun"
 * }
 * 如果提交的json格式表单内含多层entity结构，比如
 * {
 *     "id":"233",
 *     "user":{
 *         "name":"剑雨幽魂"
 *     }
 * }
 * 那么需要重新定义一个通用的JsonBodyCreator
 * 例如：现拥有工具类JsonUtil作为json转entity用。
 * 那么onCreate的代码可以如下编写
 *  JSONObject jsonObject = new JSONObject();
 *         for (String key : params.keySet()) {
 *             try {
 *                 Object value = params.get(key);
 *                 if (value == null) {
 *                     continue;
 *                 }
 *                 if (value instanceof List) {
 *                     List list = (List) value;
 *                     jsonObject.put(key, JsonUtil.toJSONArray(list));
 *                 } else if (value instanceof Integer
 *                         || value instanceof String
 *                         || value instanceof Double
 *                         || value instanceof Boolean
 *                         || value instanceof Float
 *                         || value instanceof Byte
 *                         || value instanceof Long
 *                         || value instanceof Short
 *                         || value instanceof Character) {
 *                     jsonObject.put(key, value);
 *                 } else {
 *                     jsonObject.put(key, JsonUtil.toJSONObject(value));
 *                 }
 *             } catch (JSONException e) {
 *                 e.printStackTrace();
 *             }
 *         }
 *  return FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
 * 这样即可实现多层Json结构的参数提交，详情可见FastGather项目的JsonBodyCreator
 * https://github.com/jianyuyouhun/FastGather/blob/master/app/src/main/java/com/jianyuyouhun/mobile/fastgather/JsonBodyCreator.java
 * @author wangyu
 * @date 2018/5/2
 */

@SuppressWarnings("ALL")
public class MonoLayerJsonBodyCreator implements BodyCreatorAction {

    @Override
    public RequestBody onCreate(Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject();
        for (String key : params.keySet()) {
            try {
                jsonObject.put(key, params.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
    }
}
