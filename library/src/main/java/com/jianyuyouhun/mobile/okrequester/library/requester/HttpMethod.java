package com.jianyuyouhun.mobile.okrequester.library.requester;

/**
 * http请求类型
 * @author wangyu
 */
public enum HttpMethod {
    /**请求类型*/
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");
    private String method;
    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
