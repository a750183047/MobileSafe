package com.yan.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.yan.mobilesafe.Bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 进程信息管理
 * Created by a7501 on 2015/12/18.
 */
public class TaskInfoParser {
    public static List<TaskInfo> getTaskInfos(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<TaskInfo> taskInfos = new ArrayList<>();
        //获取到进程管理器
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(context.ACTIVITY_SERVICE);
        //获取到手机上面所有的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses =
                activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo :
                runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            //获取到进程的名字
            String processName = runningAppProcessInfo.processName;
            taskInfo.setPackageName(processName);
            try {
                //获取到内存基本信息

                Debug.MemoryInfo[] memoryInfo =
                        activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                //获取到总共弄脏多少内存（当前应用占用了多少内存）；
                int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;
                taskInfo.setMemorySize(totalPrivateDirty);

                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setDrawable(icon);
                String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setAppName(appName);


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
