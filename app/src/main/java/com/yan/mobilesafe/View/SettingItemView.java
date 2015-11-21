package com.yan.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yan.mobilesafe.R;

/**
 * 设置中心的自定义组合控件
 * Created by a7501 on 2015/11/21.
 */

public class SettingItemView extends RelativeLayout {

    private TextView textSettingTitle;
    private TextView textSettingDesc;
    private CheckBox checkBoxSettingStatus;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    /**
     * 初始化布局
     * */
    private void initView(){
        //将定义好的布局设置给当前的布局
        View.inflate(getContext(), R.layout.view_setting_item, this);
        textSettingTitle = (TextView) findViewById(R.id.text_setting_title);
        textSettingDesc = (TextView) findViewById(R.id.text_setting_desc);
        checkBoxSettingStatus = (CheckBox) findViewById(R.id.checkbox_setting_status);
    }
    /**
     * 设置标题
     * */
    public void setTitle(String title){
        textSettingTitle.setText(title);
    }
    /**
     * 设置说明
     * */
    public void setDesc(String desc){
        textSettingDesc.setText(desc);
    }
    /**
     * 返回勾选状态
     * */
    public boolean isChecked(){
        return checkBoxSettingStatus.isChecked();
    }
    /**
     * 设置勾选状态
     * */
    public void setCheckBoxSettingStatus(boolean status){
        checkBoxSettingStatus.setChecked(status);

    }
}
