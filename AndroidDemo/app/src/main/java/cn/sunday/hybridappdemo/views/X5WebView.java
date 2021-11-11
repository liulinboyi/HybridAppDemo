package cn.sunday.hybridappdemo.views;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Map;

import cn.sunday.hybridappdemo.jsInterface.MyJaveScriptInterface;


public class X5WebView extends WebView {

    private Context mContext;

    public X5WebView(Context context) {
        super(context);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        init(context);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        init(context);
    }

    private void init (Context context) {
        this.mContext = context;

        /**
         * 基础配置
         */
        initWebViewSettings();
        initWebViewClient();
        initChromeClient();

        /**
         * 构建 JSBridge 对象，这里提供的 JSBridge 字符串会被挂载到
         * 网页中的 window 对象下面。
         *
         * window.AndroidJSBridge
         */
        addJavascriptInterface(
                new MyJaveScriptInterface(mContext, this),
                "AndroidJSBridge");


    }

    /**
     * 对 webview 进行基础配置
     */
    private void initWebViewSettings () {
        WebSettings webSettings = getSettings();
        /**
         * 允许加载的网页执行 JavaScript 方法
         */
        webSettings.setJavaScriptEnabled(true);
        /**
         * 设置网页不允许缩放
         */
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(true);
        /**
         * 设置网页缓存方式为不缓存，方便我们的调试
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    /**
     * 设置 webviewClient ，如果不进行这层设置，则网页打开默认会使用
     * 系统中的浏览器进行打开，而不是在本 APP 中进行打开。
     */
    private void initWebViewClient () {
        setWebViewClient(new WebViewClient(){
        });
    }

    /**
     * 监听网页中的url加载事件
     */
    private void initChromeClient () {
        setWebChromeClient(new WebChromeClient(){

            /**
             * alert()
             * 监听alert弹出框，使用原生弹框代替alert。
             */
            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(s1);
                    builder.setNegativeButton("确定", null);
                    builder.create().show();
                jsResult.confirm();

                return true;
            }
        });
    }
}
