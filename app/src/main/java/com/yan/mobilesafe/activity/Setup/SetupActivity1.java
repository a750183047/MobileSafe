package com.yan.mobilesafe.activity.Setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yan.mobilesafe.R;


/**
 * 第一个设置向导页
 * Created by yan on 2015/11/22.
 */
public class SetupActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        Button next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SetupActivity2.class));
                finish();
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

            }
        });
    }
}
