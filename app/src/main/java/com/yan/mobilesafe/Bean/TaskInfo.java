package com.yan.mobilesafe.Bean;

import android.graphics.drawable.Drawable;

/**
 * 进程信息
 * Created by a7501 on 2015/12/18.
 */
public class TaskInfo {
    private Drawable drawable;
    private String packageName;
    private String appName;
    private long memorySize;
    private boolean isUserApp; //是否为用户进程
    private boolean isChickde; //是否勾选

    public boolean isChickde() {
        return isChickde;
    }

    public void setIsChickde(boolean isChickde) {
        this.isChickde = isChickde;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "drawable=" + drawable +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", memorySize=" + memorySize +
                ", isUserApp=" + isUserApp +
                '}';
    }
}
