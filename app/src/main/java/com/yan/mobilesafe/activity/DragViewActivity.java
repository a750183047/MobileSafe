package com.yan.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yan.mobilesafe.R;

/**
 * 显示框位置拖动
 * Created by a7501 on 2015/12/2.
 */
public class DragViewActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView tvTop;
    private TextView tvButtom;
    private ImageView ivDrag;
    private int startX;
    private int startY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        tvTop = (TextView) findViewById(R.id.tv_top);
        tvButtom = (TextView) findViewById(R.id.tv_bottom);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        int lastX = sharedPreferences.getInt("lastX",0);
        int lastY = sharedPreferences.getInt("lastY",0);

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = lastX;
        layoutParams.topMargin = lastY;
        ivDrag.setLayoutParams(layoutParams);  //重新设置位置


        //设置触摸监听
        ivDrag.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        //计算偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;
                        //更新上下左右距离
                        int l = ivDrag.getLeft() + dx;
                        int r = ivDrag.getRight() + dx;
                        int t = ivDrag.getTop() + dy;
                        int b = ivDrag.getBottom() + dy;
                        //更新界面
                        ivDrag.layout(l,t,r,b);
                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //记录坐标点
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("lastX",ivDrag.getLeft());
                        editor.putInt("lastY",ivDrag.getTop());
                        editor.apply();
                        break;
                }

                return true;
            }
        });

    }
}
