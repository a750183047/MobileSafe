package com.yan.mobilesafe.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.yan.mobilesafe.Bean.TaskInfo;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.AppTaskManagerHeadersAdapter;
import com.yan.mobilesafe.adapter.listentr.MyItemClickListener;
import com.yan.mobilesafe.engine.TaskInfoParser;

import java.util.ArrayList;
import java.util.List;


public class TaskManager extends Fragment {

    private View view;
    private Context context;
    private TextView taskRun;
    private TextView memory;
    private RecyclerView recyclerVeiw;
    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userApp;
    private List<TaskInfo> systemApp;
    private Button selectAll;
    private AppTaskManagerHeadersAdapter appTaskManagerHeadersAdapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initUI(inflater, container);

        return view;

    }

    void initUI(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_task_manager, container, false);
        taskRun = (TextView) view.findViewById(R.id.tv_task);
        memory = (TextView) view.findViewById(R.id.tv_mem);
        selectAll = (Button) view.findViewById(R.id.select_all);
        recyclerVeiw = (RecyclerView) view.findViewById(R.id.view_rec);

        /**
         * 全选按钮监听
         */
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TaskInfo tastk :
                        userApp) {
                    tastk.setIsChickde(true);
                }
                for (TaskInfo task :
                        systemApp) {
                    task.setIsChickde(true);
                }
                appTaskManagerHeadersAdapter.notifyDataSetChanged();

            }
        });

    }




    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            appTaskManagerHeadersAdapter = new AppTaskManagerHeadersAdapter(context, userApp,systemApp);
            recyclerVeiw.addItemDecoration(new StickyRecyclerHeadersDecoration(appTaskManagerHeadersAdapter));
            recyclerVeiw.setAdapter(appTaskManagerHeadersAdapter);

            /**
             * 列表条目监听
             */
            appTaskManagerHeadersAdapter.setOnItemClickListener(new MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position < userApp.size()) {
                        TaskInfo taskInfo = userApp.get(position);
                        if (taskInfo.isChickde()) {
                            taskInfo.setIsChickde(false);
                            //通过传进来的view 来设置 checkBox 的状态
                            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app_task);
                            checkBox.setChecked(false);
                        } else {
                            taskInfo.setIsChickde(true);
                            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app_task);
                            checkBox.setChecked(true);
                        }

                    } else {
                        TaskInfo taskInfo = systemApp.get(position - userApp.size());
                        if (taskInfo.isChickde()) {
                            taskInfo.setIsChickde(false);
                            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app_task);
                            checkBox.setChecked(false);
                        } else {
                            taskInfo.setIsChickde(true);
                            CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app_task);
                            checkBox.setChecked(true);
                        }
                    }
                }
            });

        }
    };
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void initData() {
        //得到进程管理者
        final ActivityManager systemService = (ActivityManager)
                getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        //获取当前手机正在运行的所有进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses =
                systemService.getRunningAppProcesses();
        int size = runningAppProcesses.size();     //这个API在小米的系统中已经废掉了，然后没有找到能用的API
        taskRun.setText("运行的进程：" + size);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获取到内存的基本信息
        systemService.getMemoryInfo(memoryInfo);
        //获取剩余内存
        long availMem = memoryInfo.availMem;
        long totalMem = memoryInfo.totalMem; //总内存数
        memory.setText("剩余/总内存：" + Formatter.formatFileSize(context, availMem) + "/" +
                Formatter.formatFileSize(context, totalMem));
        //初始化recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerVeiw.setLayoutManager(linearLayoutManager);
        new Thread(){
            @Override
            public void run() {
                taskInfos = new ArrayList<TaskInfo>();
                userApp = new ArrayList<TaskInfo>();
                systemApp = new ArrayList<TaskInfo>();
                TaskManager.this.taskInfos = TaskInfoParser.getTaskInfos(context);
                for (TaskInfo taskInfo :
                        taskInfos) {
                    if (taskInfo.isUserApp()) {
                        userApp.add(taskInfo);
                    }else {
                        systemApp.add(taskInfo);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


}
