package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.yan.mobilesafe.R;

public class AntivirusActivity extends AppCompatActivity {

    private ImageView scanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initUI() {
        setContentView(R.layout.activity_antivirus);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        scanning = (ImageView) findViewById(R.id.act_scanning);
        /**
        * 第一个参数表示开始的角度 第二个参数表示结束的角度 第三个参数表示参照自己 初始化旋转动画
        */
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画时间
        rotateAnimation.setDuration(3000);
        //设置为无限循环
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        scanning.startAnimation(rotateAnimation); //开始动画
    }

}
