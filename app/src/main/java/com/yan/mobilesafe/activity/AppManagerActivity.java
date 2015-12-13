package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.yan.mobilesafe.Bean.AppInfo;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.AppManagerAdapter;
import com.yan.mobilesafe.adapter.AppManagerHeadersAdapter;
import com.yan.mobilesafe.adapter.listentr.MyItemClickListener;
import com.yan.mobilesafe.engine.AppInfos;
import com.yan.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity {

    @ViewInject(R.id.view_rec)
    private RecyclerView recyclerView;
    @ViewInject(R.id.tv_rom)
    private TextView tvRom;
    @ViewInject(R.id.tv_sd)
    private TextView tvSD;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private AppManagerHeadersAdapter appManagerHeadersAdapter;
    private PopupWindow popupWindow;

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            AppManagerAdapter appManagerAdapter = new AppManagerAdapter(AppManagerActivity.this, appInfos);
            appManagerHeadersAdapter = new AppManagerHeadersAdapter(AppManagerActivity.this, userAppInfos, systemAppInfos);
            recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(appManagerHeadersAdapter));

            appManagerHeadersAdapter.setOnItemClickListener(new MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    popupWindowDismiss();
                    View contentView = View.inflate(AppManagerActivity.this, R.layout.item_popup_layout, null);
                    View parent = View.inflate(AppManagerActivity.this, R.layout.activity_app_manager, null);

                    // -2表示包裹内容
                    popupWindow = new PopupWindow(contentView, -2, -2);
                    //需要注意：使用PopupWindow 必须设置背景。不然没有动画
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    int[] lo = new int[2];
                    //获取view展示到窗体上面的位置
                    view.getLocationInWindow(lo);
                    popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 95, lo[1] - 45);

                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                    sa.setDuration(300);

                    contentView.startAnimation(sa);

                }
            });
            recyclerView.setAdapter(appManagerHeadersAdapter);
        }
    };




    /**
     * 初始化数据
     */
    @TargetApi(Build.VERSION_CODES.M)
    void initData() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                popupWindowDismiss();
            }
        });



        new Thread() {
            @Override
            public void run() {
                appInfos = AppInfos.getAppInfos(AppManagerActivity.this);
                userAppInfos = new ArrayList<AppInfo>(); //用户app
                systemAppInfos = new ArrayList<AppInfo>();
                for (AppInfo appInfo :
                        appInfos) {
                    if (appInfo.isUserApp()) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    /***
     * 关闭popupWindow
     */
    private void popupWindowDismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }


}
