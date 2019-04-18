package com.jianyuyouhun.mobile.okrequester.library.download;


import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;
import com.jianyuyouhun.mobile.okrequester.library.progress.OnProgressListener;
import com.jianyuyouhun.mobile.okrequester.library.progress.ProgressHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 断点续传下载请求
 * Created by wangyu on 2018/6/22.
 */

public class ContinuableDownloadRequester {

    private OnProgressListener onProgressListener;
    private String url;
    private String filePath;
    private OkHttpClient okHttpClient;
    private Call call;
    private HttpHolder httpHolder = HttpHolder.getInstance();

    /**
     * 构造器，使用断点续传建议先存入临时路径，待下载完成后改到正式路径
     * 所以这里filePath传入xxx.tmp较好，等监听中完成下载后改为目标路径
     *
     * @param url                url
     * @param filePath           文件路径
     * @param onProgressListener 监听
     */
    public ContinuableDownloadRequester(String url, String filePath, OnProgressListener onProgressListener) {
        this.url = url;
        this.filePath = filePath;
        this.onProgressListener = onProgressListener;
        okHttpClient = getProgressClient();
    }

    private Call newCall(long startPoints) {
        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + startPoints + "-")
                .build();
        return okHttpClient.newCall(request);
    }

    private OkHttpClient getProgressClient() {
        Interceptor interceptor = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(ProgressHelper.withProgress(originalResponse.body(), onProgressListener))
                    .build();
        };
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
    }

    public void download(final long startsPoint) {
        call = newCall(startsPoint);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                httpHolder.getHandler().post(() -> onProgressListener.onFailed(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                httpHolder.getExecutorService().execute(() -> save(response, startsPoint));
            }
        });
    }

    public void pause() {
        if (call != null) {
            call.cancel();
            httpHolder.getHandler().post(() -> onProgressListener.onFailed(new RuntimeException("download canceled")));
        }
    }

    private void save(Response response, long startsPoint) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            File tmpFile = new File(filePath);
            randomAccessFile = new RandomAccessFile(tmpFile, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
