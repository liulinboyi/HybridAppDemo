package cn.sunday.hybridappdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.sunday.hybridappdemo.constants.Constants;
import cn.sunday.hybridappdemo.views.X5WebView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private X5WebView mWebView;
    protected QRCodeView.Delegate mDelegate;

    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    /**
     * 初始化 webview
     */
    private void init() {
        mWebView = findViewById(R.id.web_view);
        mWebView.loadUrl(Constants.WEB_URL);
    }

    //    原生点击按钮，打开扫描
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.test_scan_qrcode:
//                startActivityForResult(new Intent(this, TestScanActivity.class), 1);
//                break;
//        }
//    }

    public void webwiewScan() {
        startActivityForResult(new Intent(this, TestScanActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnData = data.getStringExtra("data_return");
                    Log.d("FirstActivity", returnData);

                    Log.d("扫描结果：", returnData);
                    Toast t = Toast.makeText(getApplicationContext(), "返回后当前页面的结果：" + returnData,
                            Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

                    callSetScan(returnData);
                }
                break;
            default:
        }
    }


    public void callSetScan(String returnData) {
        StringBuilder s = new StringBuilder();
        s.append("javascript:setScan(");
        s.append("'");
        s.append(returnData);
        s.append("'");
        s.append(")");
        Log.d("caller", s.toString());
        mWebView.evaluateJavascript(s.toString(), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("setScan", s);//Debug  调试
            }
        });
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

    public void callback(Number id, String msg) {
        String call = "javascript:call(" + id + ",'" + msg + "')";
        Log.d("call", call);
        mWebView.evaluateJavascript(call, new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String s) {

            }
        });
    }

}
