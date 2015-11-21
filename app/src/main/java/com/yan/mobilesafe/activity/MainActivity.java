package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;


/**
 * splash活动
 * 2015-11-18
 */
public class MainActivity extends Activity {

    private static final int CODE_UPDATE_DIALOG = 0;  //显示对话框消息
    private static final int CODE_URL_ERROR = 1;  //Url错误
    private static final int CODE_INTERNET_ERROR = 2;  //网络错误
    private static final int CODE_JSON_ERROR = 3; //解析错误
    private static final int CODE_NO_UP_UPDATE = 4; //没有更新
    TextView version;
    private ProgressBar downloadProgress;
    String versionName;
    int versionCode;
    //服务器返回的信息
    String versionNameFromInternet;
    int versionCodeFromInternet;
    String descriptionFromInternet;
    String downloadUrlFromInternet;

    PackageInfo packageInfo;
    static final String UPDATEURL = "http://qingxinchengming.cn/appweb/update.json";
    static final String DOWNLOADURL = "http://qingxinchengming.cn/appweb/update.apk";

    private SharedPreferences sharedPreferences;  //判断自动更新是否勾选

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showUpdateDialog();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "网络异常，请检查", Toast.LENGTH_SHORT).show();
                    enterHomeActivity();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "网络错误，请检查", Toast.LENGTH_SHORT).show();
                    enterHomeActivity();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "解析错误，请检查", Toast.LENGTH_SHORT).show();
                    enterHomeActivity();
                    break;
                case 4:
                    enterHomeActivity();
                    break;
            }
        }
    };
    private boolean autoUpdate;
    private Message message;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        version = (TextView) findViewById(R.id.text_version);
        downloadProgress = (ProgressBar) findViewById(R.id.progressBarDownload);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        //获取版本号等信息
        PackageManager packageManager = getPackageManager();  //得到包的信息
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);  //得到包的信息
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version.setText("版本号：" + versionName);

        autoUpdate = sharedPreferences.getBoolean("auto_update",true);

        if (autoUpdate){
            checkVersion(); //检查版本
        }else {
            handler.sendEmptyMessageDelayed(CODE_NO_UP_UPDATE,3000);
        }



    }

    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVersion() {

        final long startTime = System.currentTimeMillis();  //获取当前时间

        new Thread() {
            @Override
            public void run() {
                message = Message.obtain();
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(UPDATEURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//设置请求方法
                    connection.setConnectTimeout(5000); //设置连接超时
                    connection.setReadTimeout(5000); //设置响应超时
                    connection.connect(); //连接服务器

                    int responseCode = connection.getResponseCode(); //获得响应码
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();  //获取输入流
                        String result = StreamUtils.readFromStream(inputStream); //把输入流转成字符串

                        //解析json
                        JSONObject jsonObject = new JSONObject(result);
                        versionNameFromInternet = jsonObject.getString("versionName");
                        versionCodeFromInternet = jsonObject.getInt("versionCode");
                        descriptionFromInternet = jsonObject.getString("description");
                        downloadUrlFromInternet = jsonObject.getString("downloadUrl");

                        if (versionCodeFromInternet > versionCode) {
                            message.what = CODE_UPDATE_DIALOG;
                        } else {
                            message.what = CODE_NO_UP_UPDATE;
                        }
                        Log.e("MainActivity", "result = " + versionNameFromInternet);

                    }
                } catch (MalformedURLException e) {
                    //url错误的异常
                    message.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误异常
                    message.what = CODE_INTERNET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();  //获得当前时间
                    long timeUsed = endTime - startTime;   //计算验证版本所用时间
                    if (timeUsed < 3000) { //如果小于3S
                        try {
                            Thread.sleep(3000 - timeUsed);   //延时到3秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(message);
                    if (connection != null) {
                        connection.disconnect();
                    }

                }

            }
        }.start();


    }

    /**
     * 显示提示框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现更新");
        builder.setMessage(descriptionFromInternet);
        builder.setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "正在下载更新", Toast.LENGTH_SHORT).show();
                downloadProgress.setVisibility(View.VISIBLE);
                downloadUpdate();
            }
        });
        builder.setPositiveButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHomeActivity();  //跳转到主界面
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHomeActivity();
            }
        });
        builder.show();
    }

    /**
     * 跳转到主界面
     */
    private void enterHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * 下载更新
     * *
     */
    private void downloadUpdate() {
        String path = Environment.getExternalStorageDirectory() + "/MobileSafe/update.apk"; //存储地址

        //xUtils
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(DOWNLOADURL, path, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //跳转到下载页面
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(responseInfo.result),
                        "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);   //如果取消安装，会返回结果，调用 onActivityResult方法
                Log.e("MainActivity", "下载成功");
            }

            @Override
            public void onFailure(HttpException e, String s) {

                Log.e("MainActivity", "下载失败");
            }

            //下载进度更新
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                downloadProgress.setProgress((int) (current * 100 / total));
                //  Log.e("MainActivity", "下载进度 " + current + "/" + total);
            }
        });

    }

    /**
     * 回调方法，取消安装调用
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHomeActivity();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
