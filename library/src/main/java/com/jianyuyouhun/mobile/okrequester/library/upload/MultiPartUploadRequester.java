package com.jianyuyouhun.mobile.okrequester.library.upload;


import com.jianyuyouhun.mobile.okrequester.library.listener.OnResultListener;
import com.jianyuyouhun.mobile.okrequester.library.progress.AbstractOnProgressListener;
import com.jianyuyouhun.mobile.okrequester.library.progress.ProgressHelper;
import com.jianyuyouhun.mobile.okrequester.library.requester.BaseJsonRequester;
import com.jianyuyouhun.mobile.okrequester.library.requester.HttpMethod;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.BodyCreator;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.RequestMethod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.RequestBody;

import static com.jianyuyouhun.mobile.okrequester.library.upload.MultiPartBodyCreator.KEY_EXTRAS;
import static com.jianyuyouhun.mobile.okrequester.library.upload.MultiPartBodyCreator.KEY_FILES;


/**
 * 上传文件请求
 *
 * @author wangyu
 * @date 2018/6/21
 */
@SuppressWarnings("ALL")
@BodyCreator(MultiPartBodyCreator.class)
@RequestMethod(value = HttpMethod.POST, isFinal = true)
public abstract class MultiPartUploadRequester<T> extends BaseJsonRequester<T> {

    private AbstractOnProgressListener abstractOnProgressListener;

    public MultiPartUploadRequester(@NonNull AbstractOnProgressListener abstractOnProgressListener, @NonNull OnResultListener<T> listener) {
        super(listener);
        this.abstractOnProgressListener = abstractOnProgressListener;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        Map<String, Object> extraMap = new HashMap<>();
        onPutExtraParams(extraMap);
        params.put(KEY_EXTRAS, extraMap);
        Map<String, File> fileMap = new HashMap<>();
        onPutFiles(fileMap);
        params.put(KEY_FILES, fileMap);
    }

    /**
     * 填充文件
     *
     * @param fileMap
     */
    protected abstract void onPutFiles(Map<String, File> fileMap);

    /**
     * 填充参数
     *
     * @param params
     */
    protected abstract void onPutExtraParams(Map<String, Object> params);

    @NonNull
    @Override
    protected RequestBody onBuildRequestBody(Map<String, Object> params) {
        return ProgressHelper.withProgress(super.onBuildRequestBody(params), abstractOnProgressListener);
    }
}
