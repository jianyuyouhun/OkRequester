package com.jianyuyouhun.mobile.okrequester.library.requester;

import android.support.annotation.NonNull;
import android.util.Xml;


import com.jianyuyouhun.mobile.okrequester.library.listener.OnResultListener;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

/**
 * xml格式请求基类
 * Created by wangyu on 2018/6/25.
 */

public abstract class BaseXmlRequester<T> extends BaseRequester<T, XmlPullParser> {

    public BaseXmlRequester(@NonNull OnResultListener<T> listener) {
        super(listener);
    }

    @Override
    protected XmlPullParser parseIn(@NonNull String content) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new StringReader(content));
        return parser;
    }

}
