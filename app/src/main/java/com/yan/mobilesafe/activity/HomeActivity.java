package com.yan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import com.yan.mobilesafe.Fragment.FragmentButton;
import com.yan.mobilesafe.Fragment.FragmentIndex;
import com.yan.mobilesafe.Fragment.TaskManager;
import com.yan.mobilesafe.R;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;


/**
 * HomeActivity
 * Created by a7501 on 2015/11/19.
 */
public class HomeActivity extends MaterialNavigationDrawer {
    @Override
    public void init(Bundle bundle) {
        setDrawerHeaderImage(R.drawable.title);
        //setFirstAccountPhoto(getResources().getDrawable(R.drawable.photo));

        // create sections
        this.addSection(newSection("进程管理", R.drawable.taskmanager_home, new TaskManager()));
        this.addSection(newSection("流量统计", R.drawable.netmanager_home, new FragmentButton()));
        this.addSection(newSection("手机防盗", R.drawable.safe_home, new Intent(this,SimGuard.class)));
        this.addSection(newSection("通讯卫士",R.drawable.callmsgsafe_home,new Intent(this,CallSafeActivity.class)));
        this.addSection(newSection("软件管理", R.drawable.apps_home, new Intent(this,AppManagerActivity.class)));
        this.addSection(newSection("手机杀毒", R.drawable.trojan_home, new FragmentButton()));
        this.addSection(newSection("缓存清理", R.drawable.sysoptmize, new FragmentButton()));
        this.addSection(newSection("高级工具", R.drawable.tools_home, new Intent(this,AToolActivity.class)));
        // create bottom section
        this.addBottomSection(newSection("设置中心", R.drawable.ic_settings_black_24dp, new Intent(this, SettingActivity.class)));
    }
}


