package com.jianyuyouhun.mobile.okrequester.library;

import android.util.Log;

/**
 * 日志打印
 *
 * @author wangyu
 * @date 2018/3/27
 */

public class Logger {
    public static void i(String tag, String msg) {
        if (HttpHolder.isDebug) {
            int maxStrLength = 2001 - tag.length();
            //大于4000时
            while (msg.length() > maxStrLength) {
                Log.i(tag, msg.substring(0, maxStrLength));
                msg = msg.substring(maxStrLength);
            }
            //剩余部分
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (HttpHolder.isDebug) {
            int maxStrLength = 2001 - tag.length();
            //大于4000时
            while (msg.length() > maxStrLength) {
                Log.e(tag, msg.substring(0, maxStrLength));
                msg = msg.substring(maxStrLength);
            }
            //剩余部分
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (HttpHolder.isDebug) {
            int maxStrLength = 2001 - tag.length();
            //大于4000时
            while (msg.length() > maxStrLength) {
                Log.d(tag, msg.substring(0, maxStrLength));
                msg = msg.substring(maxStrLength);
            }
            //剩余部分
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (HttpHolder.isDebug) {
            int maxStrLength = 2001 - tag.length();
            //大于4000时
            while (msg.length() > maxStrLength) {
                Log.w(tag, msg.substring(0, maxStrLength));
                msg = msg.substring(maxStrLength);
            }
            //剩余部分
            Log.w(tag, msg);
        }
    }
}
