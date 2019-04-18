package com.jianyuyouhun.mobile.okrequester.library.requester.annotation;


import com.jianyuyouhun.mobile.okrequester.library.requester.creator.BodyCreatorAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数解析器注解
 * Created by wangyu on 2018/5/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BodyCreator {
    Class<? extends BodyCreatorAction> value();
}
