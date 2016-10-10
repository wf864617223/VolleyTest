package com.example.hp.volleytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView) findViewById(R.id.tv_result);
        HashMap<String,String> parms = new HashMap<>();
        parms.put("telephone","15123309125");
        parms.put("password","123456");

        HttpUtils.doHttp(this, "http://61.139.124.246:90/Customer/CustomerInfo.aspx?action=Register ", parms, new HttpUtils.IHttpResult() {
            @Override
            public void onSuccess(String result) {
                tvResult.setText(result);
            }

            @Override
            public void onError(Throwable ex) {
                tvResult.setText("error");
            }
        });
    }
}
