package cn.sunday.hybridappdemo.jsInterface;


import android.app.AlertDialog;
import android.content.Context;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.ValueCallback;

import java.util.Timer;
import java.util.TimerTask;

import cn.sunday.hybridappdemo.views.X5WebView;

//可以被Webview调用的安卓原生方法
public class MyJaveScriptInterface {

    private Context mContext;
    private X5WebView mWebView;

    public MyJaveScriptInterface(Context context, X5WebView x5WebView) {
        this.mContext = context;
        this.mWebView = x5WebView;
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
