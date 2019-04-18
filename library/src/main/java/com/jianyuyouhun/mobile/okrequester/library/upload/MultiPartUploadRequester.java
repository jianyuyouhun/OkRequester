package com.jianyuyouhun.mobile.okrequester.library.upload;

import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;
import com.jianyuyouhun.mobile.okrequester.library.progress.OnProgressListener;
import com.jianyuyouhun.mobile.okrequester.library.progress.ProgressHelper;
import com.jianyuyouhun.mobile.okrequester.library.requester.BaseStringRequester;
import com.jianyuyouhun.mobile.okrequester.library.requester.HttpMethod;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.BodyCreator;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.RequestMethod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

import static com.jianyuyouhun.mobile.okrequester.library.upload.MultiPartBodyCreator.KEY_EXTRAS;
import static com.jianyuyouhun.mobile.okrequester.library.upload.MultiPartBodyCreator.KEY_FILES;


/**
 * 上传文件请求
 * Created by wangyu on 2018/6/21.
 */
@SuppressWarnings("ALL")
@BodyCreator(MultiPartBodyCreator.class)
@RequestMethod(value = HttpMethod.POST, isFinal = true)
public abstract class MultiPartUploadRequester extends BaseStringRequester {

    private OnProgressListener onProgressListener;

    public MultiPartUploadRequester(@NonNull OnProgressListener onProgressListener, @NonNull OnHttpResultListener listener) {
        super(listener);
        this.onProgressListener = onProgressListener;
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
        return ProgressHelper.withProgress(super.onBuildRequestBody(params), onProgressListener);
    }
}
