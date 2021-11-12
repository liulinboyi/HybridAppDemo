package cn.sunday.hybridappdemo.jsInterface;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.sunday.hybridappdemo.MainActivity;
import cn.sunday.hybridappdemo.views.X5WebView;

//可以被Webview调用的安卓原生方法
public class MyJaveScriptInterface {

    private Context mContext;
    private X5WebView mWebView;

    public MyJaveScriptInterface(Context context, X5WebView x5WebView) {
        this.mContext = context;
        this.mWebView = x5WebView;
    }

    @JavascriptInterface
    public void webwiewScan(String params) {
        try {
            JSONObject json = new JSONObject(params);
            final String msg = json.optString("msg");
            final Number id = json.getInt("callid");
            Log.d("scan", params);
            MainActivity.instance.webwiewScan();
//            新版的Android的SDK要求在创建WebView所在的线程中操作它，在其它线程中操作它都会报这样的错误
            MainActivity.instance.runOnUiThread(new Runnable() {//其中 WebViewActivity 是webview所在的Activity
                public void run() {
                    MainActivity.instance.callback(id, msg);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * window.AndroidJSBridge.androidTestFunction1('xxxx')
     * 调用该方法，APP 会弹出一个 Alert 对话框，
     * 对话框中的内容为 JavaScript 传入的字符串
     *
     * @param str android 只能接收基本数据类型参数
     *            ，不能接收引用类型的数据（Object、Array）。
     *            JSON.stringify(Object) -> String
     *            *重要*
     */
    @JavascriptInterface
    public void androidTestFunction1(String str/*安卓接收的参数只能是基本数据类型*/) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(str);
        builder.setNegativeButton("确定", null);
        builder.create().show();
    }

    /**
     * 调用该方法，方法会返回一个返回值给 javaScript 端
     *
     * @return 返回值的内容为："androidTestFunction2方法的返回值"
     */
    @JavascriptInterface
    public String androidTestFunction2() {
        return "androidTestFunction2方法的返回值";
    }

}
