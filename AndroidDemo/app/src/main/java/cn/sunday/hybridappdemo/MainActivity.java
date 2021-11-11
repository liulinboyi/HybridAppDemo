package cn.sunday.hybridappdemo;

import android.app.AlertDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sunday.hybridappdemo.constants.Constants;
import cn.sunday.hybridappdemo.views.X5WebView;


public class MainActivity extends AppCompatActivity {

    private X5WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * 初始化 webview
     */
    private void init() {
        mWebView = findViewById(R.id.web_view);
        mWebView.loadUrl(Constants.WEB_URL);
    }


    /**
     * 原生端调用 web 方法，方法必须是挂载到 web 端 window 对象下面的方法。
     * 调用 JS 中的方法：onFunction1
     */
    public void onJSFunction1(View v) {
        mWebView.evaluateJavascript("javascript:onFunction('android调用JS方法')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                Log.d("onJSFunction1", s);//Debug  调试
//                https://stackoverflow.com/questions/10267910/jsonexception-value-of-type-java-lang-string-cannot-be-converted-to-jsonobject
                String json = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1).replace("\\\"", "\"");
                Log.d("处理之后", json);//Debug  调试
                JSONObject res = null;
                try {
                    res = new JSONObject(json);

                    boolean flag = res.optBoolean("status");
                    String msg = res.optString("msg");
                    if (flag) {
                        builder.setMessage(msg);
                        builder.setNegativeButton("确定", null);
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                定时
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        builder.create().show();
//                    }
//                }, 1000);

            }
        });
    }
}
