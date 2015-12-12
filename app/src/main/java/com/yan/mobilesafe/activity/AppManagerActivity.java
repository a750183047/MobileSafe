package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yan.mobilesafe.Bean.AppInfo;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.AppManagerAdapter;
import com.yan.mobilesafe.engine.AppInfos;

import java.util.List;

public class AppManagerActivity extends AppCompatActivity {

    @ViewInject(R.id.view_rec)
    private RecyclerView recyclerView;
    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSD;
    private List<AppInfo> appInfos;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initUI();
        initData();

    }

    private void initUI() {
        setContentView(R.layout.activity_app_manager);
        ViewUtils.inject(this);
        //获取rom所剩空间
        long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
        //获取sd所剩空间
        long sd_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        //格式化数据
        tvRom.setText("手机内存可用：" + Formatter.formatFileSize(this, rom_freeSpace));
        tvSD.setText("SD内存可用：" + Formatter.formatFileSize(this, sd_freeSpace));
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            AppManagerAdapter appManagerAdapter = new AppManagerAdapter(AppManagerActivity.this, appInfos);

            recyclerView.setAdapter(appManagerAdapter);
        }
    };
    /**
     * 初始化数据
     */
    void initData(){


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        new Thread(){
            @Override
            public void run() {
                appInfos = AppInfos.getAppInfos(AppManagerActivity.this);
                handler.sendEmptyMessage(0);
            }
        }.start();



    }


}
