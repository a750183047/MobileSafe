package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yan.mobilesafe.Bean.BlackNumberInfo;
import com.yan.mobilesafe.DataBase.BlackNumberDb;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.MyAdapter;
import com.yan.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class CallSafeActivity extends AppCompatActivity implements TextWatcher {

    private ListView listView;
    private RecyclerView recyclerView;
    private BlackNumberDb blackNumberDb;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout ll_pb;
    private MyAdapter adapter;
    private List<BlackNumberInfo> blackNumberInfos;
    private int startIndex = 0;  //开始的位置
    private int maxCount = 20;  //每页展示20条数据
    private int totalNumber;
    private Button addBlackNumber;
    private Button btnOk;
    private Button btnCancel;
    private CheckBox cbModePhone;
    private CheckBox cbModeSms;
    private EditText etBlackNumber;

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
            swipeRefresh.setRefreshing(false); //设置更新完成
            switch (msg.what) {
                case 0:
                    ll_pb.setVisibility(View.INVISIBLE);
                    if (adapter == null) {
                        adapter = new MyAdapter(CallSafeActivity.this, blackNumberInfos);
                        recyclerView.setAdapter(adapter);
                    } else {
                        ///////
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    ll_pb.setVisibility(View.INVISIBLE);
                    adapter = new MyAdapter(CallSafeActivity.this, blackNumberInfos);
                    recyclerView.setAdapter(adapter);
                    break;

            }


        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        blackNumberDb = new BlackNumberDb(this);
        totalNumber = blackNumberDb.getTotalNumber(); //总共有多少条数据
        //使用多线程加载数据，防止主线程阻塞
        new Thread() {
            @Override
            public void run() {
                // blackNumberInfos = blackNumberDb.findAll();  //加载数据
                if (blackNumberInfos == null) {
                    blackNumberInfos = blackNumberDb.findPar2(startIndex, maxCount);
                } else {
                    blackNumberInfos.addAll(blackNumberDb.findPar2(startIndex, maxCount));
                }

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
        addBlackNumber = (Button) findViewById(R.id.btn_add_number);
        //设置下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //设置下拉刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        blackNumberDb = new BlackNumberDb(CallSafeActivity.this);
                        blackNumberInfos = blackNumberDb.findPar2(0, 20);
                        handler.sendEmptyMessage(1);
                    }
                }.start();
            }
        });

        //设置滑动监听
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             *
             * @param recyclerView
             * @param newState  滚动状态
             *                     SCROLL_STATE_IDLE 闲置状态
             *                     SCROLL_STATE_TOUCH_SCROLL 手指触摸的时候的状态
             *                     SCROLL_STATE_FLING 惯性
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //获取最后一天显示的数据
                        int lastItemPosition;
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        lastItemPosition = layoutManager.findLastVisibleItemPosition();
                        if (lastItemPosition == blackNumberInfos.size() - 1) {
                            Log.e("CallSafe", "加载更多数据:" + "\n" + "blackNumberInfos.size():" + blackNumberInfos.size() +
                                    "\nlastItemPosition:" + lastItemPosition + "\nnewState:" + newState);
                            //加载更多数据
                            ToastUtils.showToast(CallSafeActivity.this, "玩命加载中。。。");
                            startIndex += maxCount;
                            if (startIndex >= totalNumber) {
                                ToastUtils.showToast(CallSafeActivity.this, "没有更多数据了");
                                return;
                            }
                            initData();
                        }
                        break;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        /**
         * 添加黑名单
         */
        addBlackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBlackNumber();
            }
        });


    }

    /**
     * 添加黑名单
     */
    private void addBlackNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_black_number, null);
        etBlackNumber = (EditText) view.findViewById(R.id.et_black_number);
        etBlackNumber.addTextChangedListener(this);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        cbModePhone = (CheckBox) view.findViewById(R.id.cb_mode_phone);
        cbModeSms = (CheckBox) view.findViewById(R.id.cb_mode_sms);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_number = etBlackNumber.getText().toString();
                String mode = "";
                if (cbModePhone.isChecked() && cbModeSms.isChecked()) {
                    mode = "3";
                } else if (cbModePhone.isChecked()) {
                    mode = "2";
                } else if (cbModeSms.isChecked()) {
                    mode = "1";
                } else {
                    ToastUtils.showToast(CallSafeActivity.this, "请勾选拦截模式");
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(str_number);
                blackNumberInfo.setMode(mode);
                blackNumberInfos.add(1,blackNumberInfo);  //把添加的数据放在第一位
                blackNumberDb.addNumber(str_number,mode);
                if (adapter == null){
                    adapter = new MyAdapter(CallSafeActivity.this,blackNumberInfos);
                }else {

                    adapter.notifyItemInserted(1);

                }
                dialog.dismiss();

            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(etBlackNumber.getText().toString())) {
            btnOk.setEnabled(false);
        } else {
            btnOk.setEnabled(true);
        }

    }
}
