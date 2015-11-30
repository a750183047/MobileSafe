package com.yan.mobilesafe.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.Service.AddressService;
import com.yan.mobilesafe.View.SettingItemView;

/**
 * 设置活动
 * Created by yan on 2015/11/21.
 */
public class SettingActivity extends AppCompatActivity {

    private SettingItemView settingItemViewUpdate;  //自动更新设置
    private SettingItemView settingItemViewAddress; //归属地监听设置
    private SharedPreferences sharedPreferences;  //存储自动更新设置
    private boolean autoUpdate;
    private TextView title;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        title = (TextView) findViewById(R.id.title_back);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initAutoUpdate();
        initAddressView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    /**
     * 初始化自动更新设置
     */
    private void initAutoUpdate(){
        settingItemViewUpdate = (SettingItemView) findViewById(R.id.setting_update);
        settingItemViewUpdate.setTitle("自动更新设置");
        //判断自动更新设置状态
        autoUpdate = sharedPreferences.getBoolean("auto_update", true);
        if (autoUpdate) {
            settingItemViewUpdate.setDesc("自动更新已开启");
            settingItemViewUpdate.setCheckBoxSettingStatus(true);
        } else {
            settingItemViewUpdate.setDesc("自动更新已关闭");
            settingItemViewUpdate.setCheckBoxSettingStatus(false);

        }
        settingItemViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前勾选状态
                if (settingItemViewUpdate.isChecked()) {
                    settingItemViewUpdate.setCheckBoxSettingStatus(false);
                    settingItemViewUpdate.setDesc("自动更新已关闭");
                    sharedPreferences.edit().putBoolean("auto_update", false).apply();  //保存状态
                } else {
                    settingItemViewUpdate.setCheckBoxSettingStatus(true);
                    settingItemViewUpdate.setDesc("自动更新已开启");
                    sharedPreferences.edit().putBoolean("auto_update", true).apply();  //保存状态
                }
            }
        });

    }

    /**
     * 初始化归属地监听
     */
    private void initAddressView(){
        settingItemViewAddress = (SettingItemView) findViewById(R.id.setting_address);
        settingItemViewAddress.setTitle("电话归属地显示设置");

        settingItemViewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingItemViewAddress.isChecked()){
                    settingItemViewAddress.setCheckBoxSettingStatus(false);
                    settingItemViewAddress.setDesc("归属地显示已经关闭");
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                }else {
                    settingItemViewAddress.setCheckBoxSettingStatus(true);
                    settingItemViewAddress.setDesc("归属地显示已经开启");
                    startService(new Intent(SettingActivity.this,AddressService.class));
                }
            }
        });
    }
}
