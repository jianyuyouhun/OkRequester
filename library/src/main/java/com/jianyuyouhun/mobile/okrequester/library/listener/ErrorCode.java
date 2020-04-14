package com.jianyuyouhun.mobile.okrequester.library.listener;

/**
 * 错误码定义
 *
 * @author wangyu
 * @date 2017/12/7
 */

public interface ErrorCode {
    /** 请求成功 */
    int RESULT_DATA_OK = 0;
    /** 网络错误 */
    int RESULT_NET_ERROR = -1;
    /** json解析错误 */
    int RESULT_JSON_PARSE_EXCEPTION = -2;
    /** io异常 */
    int RESULT_IO_EXCEPTION = -3;
    /** 请求body为空 */
    int RESULT_BODY_EMPTY = -4;
}
