package com.jianyuyouhun.mobile.okrequester;

import android.os.Bundle;
import android.widget.TextView;

import com.jianyuyouhun.mobile.okrequester.library.listener.ErrorCode;
import com.jianyuyouhun.mobile.okrequester.library.requester.ApiInterface;
import com.jianyuyouhun.mobile.okrequester.library.requester.BaseRequester;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 测试页面
 * @author wangyu
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.text);
        new BaseRequester<String, String>((code, s, msg) -> {
            textView.setText(s);
        }) {

            @Override
            protected void onPutParams(@NonNull Map<String, Object> params) {

            }

            @NonNull
            @Override
            protected ApiInterface getApi() {
                return () -> "http://www.baidu.com";
            }

            @Override
            protected int parseCode(@NonNull String s) {
                return ErrorCode.RESULT_DATA_OK;
            }

            @Override
            protected String parseMessage(@NonNull String s) {
                return "";
            }

            @Override
            protected String onDumpData(@NonNull String s) throws Exception {
                return s;
            }

            @NonNull
            @Override
            protected String setRoute() {
                return "";
            }

            @Override
            protected String parseIn(@NonNull String content) throws Exception {
                return content;
            }
        }.execute();
    }
}
