package com.jianyuyouhun.mobile.okrequester.library.listener;

import android.support.annotation.Nullable;

import com.jianyuyouhun.mobile.okrequester.library.json.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 结果为json的回调处理，将json解析成泛型返回
 *
 * @param <Data>
 */
public abstract class OnJsonResultListener<Data> implements OnHttpResultListener<String> {

    private int successCode;
    private int failedCode;

    public OnJsonResultListener() {
        successCode = initSuccessCode();
        failedCode = initFailedCode();
    }

    @Override
    public final void onResult(int code, String s, String msg) {
        if (code == HTTP_OK) {
            try {
                JSONObject jsonObject = parseJsonObject(s);
                code = parseCode(jsonObject);
                msg = parseMsg(jsonObject);
                if (code == successCode) {
                    onResultData(code, onDumpData(jsonObject), msg);
                } else {
                    onResultData(code, null, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                onResultData(failedCode, null, e.getMessage());
            }
        } else {
            onResultData(failedCode, null, msg);
        }
    }

    @Override
    public final void onError(Exception e) {
        onResultData(failedCode, null, e.getMessage());
    }

    /**
     * 定义成功code
     * @return
     */
    protected abstract int initSuccessCode();

    /**
     * 定义失败code
     * @return
     */
    protected abstract int initFailedCode();

    /**
     * 回调结果处理
     * @param code
     * @param data
     * @param msg
     */
    public abstract void onResultData(int code, @Nullable Data data, String msg);

    /**
     * 解析jsonObject用于实体转换，如果只取in的部分作为json则可重写此方法处理
     * @param in    string
     * @return
     * @throws JSONException
     */
    public JSONObject parseJsonObject(String in) throws JSONException {
        return new JSONObject(in);
    }

    /**
     * 获取api-code码，默认为code，可重写此方法修改
     */
    public int parseCode(JSONObject jsonObject) {
        return jsonObject.optInt("code", failedCode);
    }

    /**
     * 获取异常信息文本，默认为msg，可重写此方法修改
     */
    public String parseMsg(JSONObject jsonObject) {
        return jsonObject.optString("msg");
    }

    /**
     * json转实体，此json是{@link #parseJsonObject(String)}的产出，可在此重写此方法修改实体转换方式
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    protected Data onDumpData(JSONObject jsonObject) {
        Class<Data> entityClass = (Class<Data>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return JsonUtil.toObject(jsonObject, entityClass);
    }
}
