package com.jianyuyouhun.mobile.okrequester.library.requester.creator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * json格式参数解析
 *
 * @author wangyu
 * @date 2018/5/2
 */

@SuppressWarnings("ALL")
public class JsonBodyCreator implements BodyCreatorAction {

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
