package com.jianyuyouhun.mobile.okrequester.library.requester;

import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;
import com.jianyuyouhun.mobile.okrequester.library.listener.ErrorCode;
import com.jianyuyouhun.mobile.okrequester.library.listener.HttpResultParser;
import com.jianyuyouhun.mobile.okrequester.library.listener.OnResultListener;
import com.jianyuyouhun.mobile.okrequester.library.listener.RequestProcessListener;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.BodyCreator;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.RequestMethod;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.Route;
import com.jianyuyouhun.mobile.okrequester.library.requester.creator.BodyCreatorAction;
import com.jianyuyouhun.mobile.okrequester.library.requester.creator.FormBodyCreator;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.net.HttpURLConnection.HTTP_OK;


/**
 * 请求基类
 * Created by wangyu on 2018/6/9.
 */

@SuppressWarnings("ALL")
@BodyCreator(FormBodyCreator.class)
public abstract class BaseRequester<Out, In> {

    private HttpHolder httpHolder = HttpHolder.getInstance();

    protected OnResultListener<Out> listener;

    private HttpResultParser httpResultParser = new HttpResultParser() {
        @Override
        public void onResult(int code, String content, @NonNull String msg) {
            for (RequestProcessListener processListener : httpHolder.getRequestProcessListeners()) {
                processListener.onResult(code, setReqUrl(), setRoute(), content);
            }
            try {
                if (code == HTTP_OK) {
                    In in = parseIn(content);
                    BaseRequester.this.onResult(parseCode(in), in, parseMessage(in));
                } else {
                    BaseRequester.this.onResult(code, null, "");
                }
            } catch (Exception exception) {
                onError(exception);
            }
        }

        @Override
        public void onError(Exception e) {
            for (RequestProcessListener processListener : httpHolder.getRequestProcessListeners()) {
                processListener.onError(setReqUrl(), setRoute(), e);
            }
            BaseRequester.this.onError(e);
        }
    };

    public BaseRequester(@NonNull OnResultListener<Out> listener) {
        this.listener = listener;
    }

    public void execute(long delay) {
        httpHolder.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        }, delay);
    }

    public void execute() {
        httpHolder.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = httpHolder.getOkHttpClient();
                Map<String, Object> params = new HashMap<>();
                onPutParams(params);
                RequestBody requestBody = null;
                String url = setReqUrl();
                HttpMethod method = setMethod();
                switch (method) {
                    case GET:
                        url = appendGetParams(url, params);
                        break;
                    case POST:
                        requestBody = onBuildRequestBody(params);
                        break;
                }
                Request.Builder reqBuilder = new Request.Builder()
                        .url(url)
                        .method(setMethod().getMethod(), requestBody);
                preHandleRequest(reqBuilder);
                Request request = reqBuilder.build();
                for (RequestProcessListener processListener : httpHolder.getRequestProcessListeners()) {
                    processListener.onPreRequest(setReqUrl(), setRoute(), params);
                }
                try {
                    final Response response = client.newCall(request).execute();
                    if (response == null) {
                        httpHolder.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                httpResultParser.onResult(ErrorCode.RESULT_BODY_EMPTY, null, "");
                            }
                        });
                    } else {
                        if (response.isSuccessful()) {
                            ResponseBody body = response.body();
                            final String result = body != null ? body.string() : "";
                            httpHolder.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    httpResultParser.onResult(response.code(), result, "");
                                }
                            });
                        } else {
                            httpHolder.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    httpResultParser.onResult(ErrorCode.RESULT_NET_ERROR, "", "");
                                }
                            });
                        }
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    httpHolder.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            httpResultParser.onError(e);
                        }
                    });
                }
            }
        });
    }

    /**
     * 获取参数
     *
     * @param params
     */
    protected abstract void onPutParams(@NonNull Map<String, Object> params);

    /**
     * 请求参数转换
     *
     * @param params params map形式
     * @return RequestBody
     */
    @NonNull
    protected RequestBody onBuildRequestBody(@NonNull Map<String, Object> params) {
        RequestBody body = null;
        Class<?> cls = this.getClass();
        boolean hasFoundAnnotation = false;
        while (cls != null && !hasFoundAnnotation) {
            BodyCreator creator = cls.getAnnotation(BodyCreator.class);
            if (creator == null) {
                cls = cls.getSuperclass();
                hasFoundAnnotation = false;
            } else {
                hasFoundAnnotation = true;
                try {
                    BodyCreatorAction requestBodyCreator = creator.value().newInstance();
                    body = requestBodyCreator.onCreate(params);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return body;
    }

    private String appendGetParams(String url, @NonNull Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet()) {
            builder.append(key)
                    .append("=")
                    .append(params.get(key))
                    .append("&");
        }
        url = url + "?" + builder.toString();
        return url;
    }

    /**
     * request预处理
     *
     * @param reqBuilder reqBuilder
     */
    protected void preHandleRequest(@NonNull Request.Builder reqBuilder) {

    }

    /**
     * 设置请求方式
     *
     * @return {@link HttpMethod})
     */
    @NonNull
    protected HttpMethod setMethod() {
        Class<?> cls = this.getClass();
        LinkedList<RequestMethod> requestMethods = new LinkedList<>();
        while (cls != null && cls != Object.class) {//找到所有注解
            RequestMethod requestMethod = cls.getAnnotation(RequestMethod.class);
            if (requestMethod != null) {
                requestMethods.addLast(requestMethod);
            }
            cls = cls.getSuperclass();
        }
        HttpMethod method;
        if (requestMethods.size() == 0) {//未注解默认GET请求
            method = HttpMethod.GET;
        } else {
            RequestMethod finalMethod = null;
            for (int i = requestMethods.size() - 1; i >= 0; i--) {//找到最早定义的final注解
                RequestMethod requestMethod = requestMethods.get(i);
                if (requestMethod.isFinal()) {
                    finalMethod = requestMethod;
                    break;
                }
            }
            if (finalMethod == null) {//不存在final注解的话则采用最新的注解
                method = requestMethods.get(0).value();
            } else {//存在final注解则采用final注解
                method = finalMethod.value();
            }
        }
        return method;
    }

    /**
     * 获取请求api
     *
     * @return {@link ApiInterface}
     */
    @NonNull
    protected abstract ApiInterface getApi();

    /**
     * 设置请求url
     *
     * @return url拼接路由得到的请求地址
     */
    @NonNull
    protected String setReqUrl() {
        return getApi().getApiUrl() + setRoute();
    }

    /**
     * 设置请求路由
     *
     * @return route
     */
    @NonNull
    protected String setRoute() {
        String result = null;
        Class<?> cls = getClass();
        boolean hasFoundAnnotation = false;
        while (cls != null && !hasFoundAnnotation) {
            Route route = cls.getAnnotation(Route.class);
            if (route == null) {
                cls = cls.getSuperclass();
                hasFoundAnnotation = false;
            } else {
                hasFoundAnnotation = true;
                result = route.value();
            }
        }
        if (result == null)
            throw new RuntimeException(this.getClass().getSimpleName() + "缺少路由配置, 请确保重写了setRoute方法或者使用了@Route注解");
        return result;
    }

    /**
     * 统一解析请求的code码
     *
     * @param in 服务端返回的数据
     * @return code
     */
    protected abstract int parseCode(@NonNull In in);

    /**
     * 统一解析请求的message
     *
     * @param in 服务端返回的数据
     * @return msg
     */
    protected abstract String parseMessage(@NonNull In in);

    /**
     * 请求失败了
     *
     * @param exception 失败原因
     */
    protected void onError(@NonNull Exception exception) {
        exception.printStackTrace();
        listener.onResult(OnResultListener.RESULT_NET_ERROR, null, exception.getMessage());
    }


    /**
     * 请求成功了  服务器已经返回结果
     *
     * @param code 请求成功的HTTP返回吗，，一般code等于200表示请求成功
     * @param in   从服务器获取到的数据
     */
    protected void onResult(int code, In in, String msg) throws Exception {
        Out data = null;
        if (code == OnResultListener.RESULT_DATA_OK) {
            data = onDumpData(in);
        }
        listener.onResult(code, data, msg);
    }

    protected abstract Out onDumpData(@NonNull In in) throws Exception;

    /**
     * 把服务器返回的数据改为要的格式
     *
     * @param content
     * @return
     */
    protected abstract In parseIn(@NonNull String content) throws Exception;
}