package com.jianyuyouhun.mobile.okrequester.library.requester.annotation;


import com.jianyuyouhun.mobile.okrequester.library.requester.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求类型注解
 *
 * @author wangyu
 * @date 2018/5/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestMethod {
    /**
     * 请求方式
     * @return  HttpMethod
     */
    HttpMethod value();

    /**
     * 是否禁止重写
     * @return final属性
     */
    boolean isFinal() default false;
}

