package com.jianyuyouhun.mobile.okrequester.library.upload;


import com.jianyuyouhun.mobile.okrequester.library.requester.creator.BodyCreatorAction;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 文件上传请求体封装
 * Created by wangyu on 2018/6/21.
 */

@SuppressWarnings("ALL")
public class MultiPartBodyCreator implements BodyCreatorAction {
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_FILES = "files";

    @Override
    public RequestBody onCreate(Map<String, Object> params) {
        Map<String, File> fileMap = (Map<String, File>) params.get(KEY_FILES);
        Map<String, Object> extraMap = (Map<String, Object>) params.get(KEY_EXTRAS);
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.ALTERNATIVE);
        for (String key : extraMap.keySet()) {
            bodyBuilder.addFormDataPart(key, extraMap.get(key).toString());
        }
        for (String key : fileMap.keySet()) {
            File file = fileMap.get(key);
            MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
            RequestBody fileBody = RequestBody.create(type, file);
            bodyBuilder.addFormDataPart(key, file.getName(), fileBody);
        }
        return bodyBuilder.build();
    }
}
