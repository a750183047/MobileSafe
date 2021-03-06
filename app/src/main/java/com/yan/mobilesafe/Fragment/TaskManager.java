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
import com.yan.mobilesafe.utils.ToastUtils;

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
    private Button selectBackAll;
    private Button killTask;
    private int size;
    private long availMem;
    private long totalMem;

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

    void initUI(LayoutInflater inflater, final ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_task_manager, container, false);
        taskRun = (TextView) view.findViewById(R.id.tv_task);
        memory = (TextView) view.findViewById(R.id.tv_mem);
        selectAll = (Button) view.findViewById(R.id.select_all);
        selectBackAll = (Button) view.findViewById(R.id.select_back_all);
        killTask = (Button) view.findViewById(R.id.kill_task);
        recyclerVeiw = (RecyclerView) view.findViewById(R.id.view_rec);

        /**
         * 全选按钮监听
         */
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TaskInfo tastk :
                        userApp) {
                    if (tastk.getPackageName().equals(context.getPackageName())){
                        continue;
                    } //如果是自己的程序，就跳过
                    tastk.setIsChickde(true);
                }
                for (TaskInfo task :
                        systemApp) {
                    task.setIsChickde(true);
                }
                appTaskManagerHeadersAdapter.notifyDataSetChanged();

            }
        });

        /**
         * 反选按钮监听
         */
        selectBackAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TaskInfo taskInfo :
                        userApp) {
                    if (taskInfo.getPackageName().equals(context.getPackageName())){
                        continue;
                    } //如果是自己的程序，就跳过
                    taskInfo.setIsChickde(!taskInfo.isChickde());
                }
                for (TaskInfo taskInfo :
                        systemApp) {
                    taskInfo.setIsChickde(!taskInfo.isChickde());
                }
                appTaskManagerHeadersAdapter.notifyDataSetChanged();
            }
        });
        killTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager = (ActivityManager)
                        context.getSystemService(Context.ACTIVITY_SERVICE);
                List<TaskInfo> killLists = new ArrayList<TaskInfo>();  //要清理的进程的链接
                //清理的总进程个数
                int totalCount = 0;
                //清理进程的大小
                int killMem = 0;
                for (TaskInfo taskInfo : userApp){
                    if (taskInfo.isChickde()){
                        killLists.add(taskInfo);
                        totalCount++;
                        killMem+=taskInfo.getMemorySize();
                    }
                }
                for (TaskInfo taskInfo : systemApp){
                    if (taskInfo.isChickde()){
                        killLists.add(taskInfo);
                        totalCount++;
                        killMem+=taskInfo.getMemorySize();
                    }
                }
                for (TaskInfo taskInfo:killLists){
                    if (taskInfo.isUserApp()){
                        userApp.remove(taskInfo);
                        activityManager.killBackgroundProcesses(taskInfo.getPackageName());
                    }else {
                        systemApp.remove(taskInfo);
                        activityManager.killBackgroundProcesses(taskInfo.getPackageName());
                    }
                }
                ToastUtils.showToast(getActivity(),"共清理"
                        + totalCount
                        + "个进程,释放"
                        + Formatter.formatFileSize(getActivity(),
                        killMem) + "内存");

                size-=totalCount;
                        taskRun.setText("运行的进程：" + size);
                memory.setText("剩余/总内存：" + Formatter.formatFileSize(context, availMem+killMem) + "/" +
                        Formatter.formatFileSize(context, totalMem));
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
        size = runningAppProcesses.size();
        taskRun.setText("运行的进程：" + size);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获取到内存的基本信息
        systemService.getMemoryInfo(memoryInfo);
        //获取剩余内存
        availMem = memoryInfo.availMem;
        totalMem = memoryInfo.totalMem;
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
