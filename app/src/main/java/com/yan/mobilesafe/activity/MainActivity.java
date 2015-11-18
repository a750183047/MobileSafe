package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.utils.StreamUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Handler;


/**
 * splash活动
 * 2015-11-18
 * */
public class MainActivity extends Activity {

    private static final int CODE_UPDATE_DIALOG = 0;  //显示对话框消息
    private static final int CODE_URL_ERROR = 1;  //Url错误
    private static final int CODE_INTERNET_ERROR = 2;  //网络错误
    private static final int CODE_JSON_ERROR = 3; //解析错误
    TextView version;
    String versionName;
    int versionCode;
    //服务器返回的信息
    String versionNameFromInternet;
    int    versionCodeFromInternet;
    String descriptionFromInternet;
    String downloadUrlFromInternet;

    PackageInfo packageInfo;
    static final String UPDATEURL = "http://qingxinchengming.cn/appweb/update.json";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    showUpdateDialog();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(),"网络异常，请检查",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"网络错误，请检查",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"解析错误，请检查",Toast.LENGTH_SHORT).show();
            }
        }
    };

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

        //获取版本号等信息
        PackageManager packageManager = getPackageManager();  //得到包的信息
        try {
          packageInfo = packageManager.getPackageInfo(getPackageName(),0);  //得到包的信息
          versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkVersion(); //检查版本



    }

    /**
     * 从服务器获取版本信息进行校验
     * */
    private void checkVersion(){

        new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(UPDATEURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//设置请求方法
                    connection.setConnectTimeout(5000); //设置连接超时
                    connection.setReadTimeout(5000); //设置响应超时
                    connection.connect(); //连接服务器

                    int responseCode =  connection.getResponseCode(); //获得响应码
                    if (responseCode == 200){
                        InputStream inputStream = connection.getInputStream();  //获取输入流
                        String result =  StreamUtils.readFromStream(inputStream); //把输入流转成字符串

                        //解析json
                        JSONObject jsonObject = new JSONObject(result);
                        versionNameFromInternet = jsonObject.getString("versionName");
                        versionCodeFromInternet = jsonObject.getInt("versionCode");
                        descriptionFromInternet = jsonObject.getString("description");
                        downloadUrlFromInternet = jsonObject.getString("downloadUrl");

                        if (versionCodeFromInternet > versionCode ){
                            message.what = CODE_UPDATE_DIALOG;
                        }
                        Log.e("MainActivity","result = "+versionNameFromInternet);

                    }
                }catch (MalformedURLException e){
                    //url错误的异常
                    message.what = CODE_URL_ERROR;
                    e.printStackTrace();
                }catch (IOException e){
                    //网络错误异常
                    message.what = CODE_INTERNET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                }finally {
                    handler.sendMessage(message);
                    if (connection != null){
                        connection.disconnect();
                    }

                }

            }
        }.start();


    }

    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现更新");
        builder.setMessage(descriptionFromInternet);
        builder.setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "正在下载更新", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "下次再说", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
