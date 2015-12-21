package com.yan.mobilesafe.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yan.mobilesafe.R;

import java.util.List;


public class TaskManager extends Fragment {

    private View view;
    private Context context;
    private TextView taskRun;
    private TextView memory;
    private RecyclerView recyclerVeiw;

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
        recyclerVeiw = (RecyclerView) view.findViewById(R.id.view_rec);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void initData() {
        //得到进程管理者
        ActivityManager systemService = (ActivityManager)
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
        memory.setText("剩余/总内存："+ Formatter.formatFileSize(context,availMem)+"/"+
                Formatter.formatFileSize(context,totalMem));


        //初始化recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerVeiw.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }
}
