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
            EasyPermissions.requestPermissions(this, "??????????????????????????????????????????????????????", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    /**
     * ????????? webview
     */
    private void init() {
        mWebView = findViewById(R.id.web_view);
        mWebView.loadUrl(Constants.WEB_URL);
    }

    //    ?????????????????????????????????
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

                    Log.d("???????????????", returnData);
                    Toast t = Toast.makeText(getApplicationContext(), "?????????????????????????????????" + returnData,
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
                Log.d("setScan", s);//Debug  ??????
            }
        });
    }


    /**
     * ??????????????? web ????????????????????????????????? web ??? window ????????????????????????
     * ?????? JS ???????????????onFunction1
     */
    public void onJSFunction1(View v) {
        mWebView.evaluateJavascript("javascript:onFunction('android??????JS??????')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                Log.d("onJSFunction1", s);//Debug  ??????
//                https://stackoverflow.com/questions/10267910/jsonexception-value-of-type-java-lang-string-cannot-be-converted-to-jsonobject
                String json = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1).replace("\\\"", "\"");
                Log.d("????????????", json);//Debug  ??????
                JSONObject res = null;
                try {
                    res = new JSONObject(json);

                    boolean flag = res.optBoolean("status");
                    String msg = res.optString("msg");
                    if (flag) {
                        builder.setMessage(msg);
                        builder.setNegativeButton("??????", null);
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                ??????
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
