package com.jianyuyouhun.mobile.okrequester.library.requester;

import android.support.annotation.NonNull;

import com.jianyuyouhun.mobile.okrequester.library.HttpHolder;
import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;
import com.jianyuyouhun.mobile.okrequester.library.listener.RequestProcessListener;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.BodyCreator;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.RequestMethod;
import com.jianyuyouhun.mobile.okrequester.library.requester.annotation.Route;
import com.jianyuyouhun.mobile.okrequester.library.requester.creator.BodyCreatorAction;
import com.jianyuyouhun.mobile.okrequester.library.requester.creator.FormBodyCreator;
import com.jianyuyouhun.mobile.okrequester.library.requester.parser.ResponseParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 请求基类，请求体只负责请求结果数据，请求结果转为字符串
 * 数据的结果处理交给listener完成
 * Created by wangyu on 2018/6/9.
 */

@SuppressWarnings("ALL")
@BodyCreator(FormBodyCreator.class)
public abstract class BaseRequester<In> {

    private HttpHolder httpHolder = HttpHolder.getInstance();

    protected OnHttpResultListener<In> listener;

    private ResponseParser<In> responseParser;

    public BaseRequester(@NonNull ResponseParser<In> responseParser, @NonNull OnHttpResultListener<In> listener) {
        this.responseParser = responseParser;
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

    /**
     * 执行请求，结果将传递到listener处理
     */
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
                    case DELETE:
                    case HEAD:
                        url = appendGetParams(url, params);
                        break;
                    case POST:
                    case PUT:
                    case PATCH:
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
                    if (HttpHolder.isDebug) {
                        if (response == null) {
                            for (RequestProcessListener processListener : httpHolder.getRequestProcessListeners()) {
                                processListener.onError(setReqUrl(), setRoute(), new NullPointerException("response is null"));
                            }
                        }
                    }
                    responseParser.onParseResponse(response, listener);
                } catch (final IOException e) {
                    e.printStackTrace();
                    if (HttpHolder.isDebug) {
                        for (RequestProcessListener processListener : httpHolder.getRequestProcessListeners()) {
                            processListener.onError(setReqUrl(), setRoute(), e);
                        }
                    }
                    responseParser.onError(e, listener);
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

}

