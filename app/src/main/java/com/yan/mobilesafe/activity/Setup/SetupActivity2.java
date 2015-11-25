package com.yan.mobilesafe.activity.Setup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.View.SettingItemView;


/**
 * 第二个设置向导页
 * Created by yan on 2015/11/22.
 */
public class SetupActivity2 extends AppCompatActivity {

    SettingItemView settingItemViewUpdate;
    private SharedPreferences sharedPreferences;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        next = (Button) findViewById(R.id.next);
        Button back = (Button) findViewById(R.id.back);
        settingItemViewUpdate = (SettingItemView) findViewById(R.id.sim_sock);

        settingItemViewUpdate.setTitle("点击绑定SIM卡");

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        //得到sim卡的序列号
        String simNumber = sharedPreferences.getString("sim", null);
        if (!TextUtils.isEmpty(simNumber)) {
            settingItemViewUpdate.setCheckBoxSettingStatus(true);
            settingItemViewUpdate.setDesc("SIM卡已经绑定");
            next.setEnabled(true);
        } else {
            settingItemViewUpdate.setCheckBoxSettingStatus(false);
            settingItemViewUpdate.setDesc("SIM卡没有绑定");
            next.setEnabled(false);
        }

        settingItemViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingItemViewUpdate.isChecked()) {
                    settingItemViewUpdate.setCheckBoxSettingStatus(false);
                    settingItemViewUpdate.setDesc("SIM卡没有绑定");
                    sharedPreferences.edit().remove("sim").apply();
                    next.setEnabled(false);
                } else {
                    settingItemViewUpdate.setCheckBoxSettingStatus(true);
                    settingItemViewUpdate.setDesc("SIM卡已经绑定");
                    TelephonyManager telephonyManager = (TelephonyManager)
                            getSystemService(TELEPHONY_SERVICE); // 得到手机信息
                    String simNumber = telephonyManager.getSimSerialNumber()  ; //获取sim序列号
                    Log.e("SetupActivity2", "Sim序列号" + simNumber);
                    sharedPreferences.edit().putString("sim", simNumber).apply();
                    next.setEnabled(true);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SetupActivity2.this, SetupActivity3.class));
                finish();
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SetupActivity2.this, SetupActivity1.class));
                finish();
                // 两个界面切换的动画
                overridePendingTransition(R.anim.tran_previous_in,
                        R.anim.tran_previous_out);// 进入动画和退出动画
            }
        });

    }

}
