package com.yan.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.yan.mobilesafe.Bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取app信息
 * Created by a7501 on 2015/12/10.
 */
public class AppInfos {
    public static List<AppInfo> getAppInfos(Context context){
        List<AppInfo> packageAppInfos = new ArrayList<AppInfo>();
        //获取包的管理者
        PackageManager packageManager = context.getPackageManager();
        //获取到安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo installedPackage :
                installedPackages) {
            AppInfo appInfo = new AppInfo();
            //图标
            Drawable drawable = installedPackage.applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(drawable);
            //应用名字
            String apdName = installedPackage.applicationInfo.loadLabel(packageManager).toString();
            appInfo.setApkName(apdName);
            //包名
            String packageName = installedPackage.packageName;
            appInfo.setApkPackageName(packageName);
            //路径
            String sourceDir = installedPackage.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            //apk 大小
            long apkSize = file.length();
            appInfo.setApkSize(apkSize);

            //获取到安装应用程序的标记
            int flags = installedPackage.applicationInfo.flags;

            if((flags & ApplicationInfo.FLAG_SYSTEM) !=0 ){
                //表示系统app
                appInfo.setUserApp(false);
            }else{
                //表示用户app
                appInfo.setUserApp(true);
            }

            if((flags& ApplicationInfo.FLAG_EXTERNAL_STORAGE) !=0 ){
                //表示在sd卡
                appInfo.setIsRom(false);
            }else{
                //表示内存
                appInfo.setIsRom(true);
            }

            packageAppInfos.add(appInfo);
        }

        return packageAppInfos;
    }
}
