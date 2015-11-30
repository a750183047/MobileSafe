package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.activity.Atool.AddressActivity;

/**
 * 高级工具
 * Created by a7501 on 2015/11/27.
 */
public class AToolActivity extends AppCompatActivity {

    private Button address;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_atoolactivity);

        address = (Button) findViewById(R.id.btn_address);


        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AToolActivity.this, AddressActivity.class));
            }
        });
    }

}
