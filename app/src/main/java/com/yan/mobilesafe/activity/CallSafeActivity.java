package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yan.mobilesafe.Bean.BlackNumberInfo;
import com.yan.mobilesafe.DataBase.BlackNumberDb;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class CallSafeActivity extends AppCompatActivity {

    private ListView listView;
    private RecyclerView recyclerView;
    private BlackNumberDb blackNumberDb;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout ll_pb;
    private MyAdapter adapter;
    private List<BlackNumberInfo> blackNumberInfos;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initListView();
        initData();

    }


    //更新表格
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            ll_pb.setVisibility(View.INVISIBLE);
            swipeRefresh.setRefreshing(false); //设置更新完成
            if (adapter == null){
                adapter = new MyAdapter(CallSafeActivity.this, blackNumberInfos);
                recyclerView.setAdapter(adapter);
            }else {
                ///////
            }

        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        blackNumberDb = new BlackNumberDb(this);
        //使用多线程加载数据，防止主线程阻塞
        new Thread() {
            @Override
            public void run() {
                blackNumberInfos = blackNumberDb.findAll();  //加载数据
                handler.sendEmptyMessage(0);
            }

        }.start();

    }

    /**
     * 初始化ListView
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void initListView() {
        //listView = (ListView) findViewById(R.id.list_view);
        //加载数据的圆圈
        ll_pb = (LinearLayout) findViewById(R.id.ll_pb);
        ll_pb.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        //设置下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //设置下拉刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        //设置滑动监听
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("CallSafe", "newState:" + newState + "; RecyclerView.SCROLL_STATE_IDLE:" + RecyclerView.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition;
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastItemPosition = layoutManager.findLastVisibleItemPosition();
                Log.e("CallSafe", "最后一个" + lastItemPosition);
            }
        });


    }

}
