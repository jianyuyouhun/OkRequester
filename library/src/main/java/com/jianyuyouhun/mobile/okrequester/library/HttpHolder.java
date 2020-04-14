package com.jianyuyouhun.mobile.okrequester.library;

import android.os.Handler;
import android.os.Looper;

import com.jianyuyouhun.mobile.okrequester.library.listener.DefaultProcessListener;
import com.jianyuyouhun.mobile.okrequester.library.listener.RequestProcessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;

/**
 * http执行环境
 *
 * @author wangyu
 * @date 2018/6/9
 */

public class HttpHolder {
    public static boolean isDebug = BuildConfig.DEBUG;
    private ExecutorService executorService;
    private Handler handler;
    private OkHttpClient okHttpClient;
    private List<RequestProcessListener> requestProcessListeners;

    private static HttpHolder instance = null;

    public synchronized static HttpHolder getInstance() {
        if (instance == null) {
            instance = new HttpHolder();
        }
        return instance;
    }

    private HttpHolder() {
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper());
        requestProcessListeners = new ArrayList<>();
        registerRequestProcessListener(new DefaultProcessListener());
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Handler getHandler() {
        return handler;
    }

    public synchronized OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public void registerRequestProcessListener(RequestProcessListener listener) {
        requestProcessListeners.add(listener);
    }

    public void unregisterRequestProcessListener(RequestProcessListener listener) {
        requestProcessListeners.remove(listener);
    }

    public List<RequestProcessListener> getRequestProcessListeners() {
        return requestProcessListeners;
    }
}
