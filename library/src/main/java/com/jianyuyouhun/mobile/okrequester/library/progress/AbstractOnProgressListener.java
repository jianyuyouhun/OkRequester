package com.jianyuyouhun.mobile.okrequester.library.progress;

import android.os.Handler;

import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;

import androidx.annotation.IntRange;


/**
 * 进度监听回调
 * <p>
 *
 * @author wangyu
 * @date 2018/6/21
 */

public abstract class AbstractOnProgressListener implements ProgressCallback {
    private boolean started;
    private long lastRefreshTime = 0L;
    private long lastBytesWritten = 0L;
    private static final double HUNDRED_PERCENT = 1;
    /** 建议最小回调时间100ms，避免频繁回调 */
    private int minTime;

    private Handler handler = HttpHolder.getInstance().getHandler();

    public AbstractOnProgressListener(@IntRange(from = 0, to = Integer.MAX_VALUE) int duration) {
        this.minTime = duration;
    }

    public AbstractOnProgressListener() {
        this(100);
    }

    @Override
    public void onProgressChanged(final long numBytes, final long totalBytes, final float percent) {
        if (!started) {
            handler.post(() -> onProgressStart(totalBytes));
            started = true;
        }
        if (numBytes == -1 && totalBytes == -1 && percent == -1) {
            handler.post(() -> onProgressChanged(-1, -1, -1, -1));
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefreshTime >= minTime || numBytes == totalBytes || percent >= 1F) {
            long intervalTime = (currentTime - lastRefreshTime);
            if (intervalTime == 0) {
                intervalTime += 1;
            }
            long updateBytes = numBytes - lastBytesWritten;
            final long networkSpeed = updateBytes / intervalTime;
            handler.post(() -> onProgressChanged(numBytes, totalBytes, percent, networkSpeed));
            lastRefreshTime = System.currentTimeMillis();
            lastBytesWritten = numBytes;
        }
        if (numBytes == totalBytes || percent >= HUNDRED_PERCENT) {
            handler.post(this::onProgressFinish);
        }
    }

    /**
     * @param numBytes   已读/写大小
     * @param totalBytes 总大小
     * @param percent    百分比
     * @param speed      速度 bytes/ms
     */
    public abstract void onProgressChanged(long numBytes, long totalBytes, float percent, float speed);

    /**
     * 进度开始
     *
     * @param totalBytes 总大小
     */
    public void onProgressStart(long totalBytes) {
    }

    /**
     * 进度结束
     */
    public void onProgressFinish() {
    }

    /**
     * 下载失败
     *
     * @param e 异常信息
     */
    public abstract void onFailed(Exception e);
}
