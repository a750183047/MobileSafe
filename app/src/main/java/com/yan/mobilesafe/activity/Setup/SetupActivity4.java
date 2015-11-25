package com.yan.mobilesafe.activity.Setup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.activity.SimGuard;


/**
 * 第四个设置向导页
 * Created by yan on 2015/11/22.
 */
public class SetupActivity4 extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private CheckBox checkBox;
    private boolean protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        Button next = (Button) findViewById(R.id.next);
        Button back = (Button) findViewById(R.id.back);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        protect = sharedPreferences.getBoolean("protect",false);
        checkBox = (CheckBox) findViewById(R.id.cb_clock);

        if (protect){
            checkBox.setText("防盗保护已经开启");
            checkBox.setChecked(true);
        }else {
            checkBox.setText("防盗保护没有开启");
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBox.setText("防盗保护已经开启");
                    sharedPreferences.edit().putBoolean("protect",true).apply();
                }else {
                    checkBox.setText("防盗保护没有开启");
                    sharedPreferences.edit().putBoolean("protect",false).apply();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SimGuard.class));
                finish();
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
                sharedPreferences.edit().putBoolean("configed", true).apply();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SetupActivity3.class));
                finish();
                // 两个界面切换的动画
                overridePendingTransition(R.anim.tran_previous_in,
                        R.anim.tran_previous_out);// 进入动画和退出动画
            }
        });
    }
}
