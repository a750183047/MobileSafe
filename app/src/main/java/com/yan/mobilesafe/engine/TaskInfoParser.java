package com.yan.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.yan.mobilesafe.Bean.TaskInfo;
import com.yan.mobilesafe.R;

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
                //获取到当前应用程序的标记
                //packageInfo.applicationInfo.flags 我们写的答案
                //ApplicationInfo.FLAG_SYSTEM表示老师的该卷器
                int flags = packageInfo.applicationInfo.flags;
                //ApplicationInfo.FLAG_SYSTEM 表示系统应用程序
                if((flags & ApplicationInfo.FLAG_SYSTEM) != 0 ){
                    //系统应用
                    taskInfo.setIsUserApp(false);
                }else{
				//用户应用
                    taskInfo.setIsUserApp(true);

                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.setAppName("系统应用");
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
