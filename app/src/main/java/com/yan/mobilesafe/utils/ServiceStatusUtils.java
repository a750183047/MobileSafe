package com.yan.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * 判断服务是否运行
 * Created by a7501 on 2015/11/30.
 */
public class ServiceStatusUtils {
    public static boolean isServiceRunning(Context context,String serviceName){
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos =
                activityManager.getRunningServices(100);  //获取正在运行的所有服务的列表，最多返回100个
        for (ActivityManager.RunningServiceInfo r:runningServiceInfos) {
            String className = r.service.getClassName();  //获取服务的名称
            if (className.equals(serviceName)){
                return true;    //服务存在
            }
        }
        return false;
    }
}

