package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.activity.Atool.AddressActivity;
import com.yan.mobilesafe.utils.SmsUtils;
import com.yan.mobilesafe.utils.ToastUtils;

/**
 * 高级工具
 * Created by a7501 on 2015/11/27.
 */
public class AToolActivity extends AppCompatActivity {

    private Button address;
    private Button smsBackup;
    private ProgressDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_atoolactivity);

        address = (Button) findViewById(R.id.btn_address);
        smsBackup = (Button) findViewById(R.id.btn_sms_backup);


        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AToolActivity.this, AddressActivity.class));
            }
        });

        smsBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出一个提示框
                progressDialog = new ProgressDialog(AToolActivity.this);
                progressDialog.setTitle("短信备份中");
                progressDialog.setMessage("正在备份短信，请稍后");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();

                new Thread(){
                    @Override
                    public void run() {
                        boolean result = SmsUtils.backupSms(AToolActivity.this,new SmsUtils.BackupSMS(){

                            @Override
                            public void before(int count) {
                                progressDialog.setMax(count);

                            }

                            @Override
                            public void onBackupSMS(int process) {
                                progressDialog.setProgress(process);
                            }
                        });
                        if (result){
                            ToastUtils.showToast(AToolActivity.this,"备份成功");
                        }else {
                            ToastUtils.showToast(AToolActivity.this,"备份失败");
                        }
                        progressDialog.dismiss();
                    }
                }.start();

            }
        });
    }

}
