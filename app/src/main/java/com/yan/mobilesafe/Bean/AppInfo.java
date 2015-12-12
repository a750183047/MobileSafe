package com.yan.mobilesafe.Bean;

import android.graphics.drawable.Drawable;

/**
 * app信息对象
 * Created by a7501 on 2015/12/10.
 */
public class AppInfo {
    private Drawable icon; //图片
    private String apkName; //app名字
    private long apkSize; //app大小
    private boolean isRom; //安装位置

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    /**
     * 判断是否为用户app，如果是true，就是用户app否则，就是系统的
     */
    private boolean userApp; //
    private String apkPackageName; //包名

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", userApp=" + userApp +
                ", apkPackageName='" + apkPackageName + '\'' +
                '}';
    }
}
