package com.jianyuyouhun.mobile.okrequester;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jianyuyouhun.mobile.okrequester.library.listener.OnHttpResultListener;
import com.jianyuyouhun.mobile.okrequester.library.requester.ApiInterface;
import com.jianyuyouhun.mobile.okrequester.library.requester.BaseStringRequester;

import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.text);
        BaseStringRequester requester = new BaseStringRequester(new OnHttpResultListener<String>() {
            @Override
            public void onResult(int code, String s, String msg) {
                if (code == HTTP_OK) {
                    textView.setText(s);
                } else {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected void onPutParams(@NonNull Map<String, Object> params) {

            }

            @NonNull
            @Override
            protected String setRoute() {
                return "";
            }

            @NonNull
            @Override
            protected ApiInterface getApi() {
                return () -> "http://www.baidu.com";
            }
        };
        requester.execute();
    }
}
