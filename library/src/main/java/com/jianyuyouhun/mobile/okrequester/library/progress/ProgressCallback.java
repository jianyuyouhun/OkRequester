package com.jianyuyouhun.mobile.okrequester.library.progress;

/**
 * 进度接口
 * Created by wangyu on 2018/6/21.
 */

public interface ProgressCallback {

    /**
     * 进度发生了改变，如果都为-1，则表示总大小获取不到
     *
     * @param numBytes   已读/写大小
     * @param totalBytes 总大小
     * @param percent    百分比
     */
    void onProgressChanged(long numBytes, long totalBytes, float percent);
}
