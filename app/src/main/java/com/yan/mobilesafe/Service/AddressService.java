package com.yan.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yan.mobilesafe.DataBase.AddressDB;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.utils.ToastUtils;

/**
 * 监听电话服务
 * Created by a7501 on 2015/11/30.
 */
public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private MyListener listener;
    private OutCallReceiver receiver;
    private WindowManager windowManager;
    private View view;
    private int startY;
    private int startX;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);  //监听来电状态

        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);  //动态注册广播

    }


    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: //电话响了
                    Log.e("AddressService", "来电话了");
                    String address = AddressDB.getAddress(incomingNumber);
                    //ToastUtils.showToast(getApplicationContext(), address);
                    showToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE: //电话闲置状态
                    if (windowManager != null) {
                        windowManager.removeView(view);  //移除view
                    }

            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    /***
     * 监听去电广播接收者
     */
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDB.getAddress(number);
            // ToastUtils.showToast(context, address);
            showToast(address);
        }
    }

    /**
     * 自定义归属地浮窗
     */
    private void showToast(String string) {
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        //获取屏幕宽高
        final int winWidth = windowManager.getDefaultDisplay().getWidth();
        final int winHeight = windowManager.getDefaultDisplay().getHeight();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;   //设置为电话窗口，用于电话交互  需要权限android.permission.SYSTEM_ALERT_WINDOW
        params.gravity = Gravity.LEFT + Gravity.TOP;  //将中心位置设置为左上角
        params.setTitle("Toast");

        int lastX = getSharedPreferences("config", MODE_PRIVATE).getInt("lastX", 0);
        int lastY = getSharedPreferences("config", MODE_PRIVATE).getInt("lastY", 0);

        //设置浮窗的位置
        params.x = lastX;
        params.y = lastY;

        view = View.inflate(this, R.layout.toast_layout, null);

        int[] bgs = new int[]{R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green};
        int style = getSharedPreferences("config", MODE_PRIVATE).getInt("address_style", 0);
        TextView textView = (TextView) view.findViewById(R.id.tv_number);
        view.setBackgroundResource(bgs[style]);
        textView.setText(string);
        windowManager.addView(view, params);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算偏移量
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        int dx = endX - startX;
                        int dy = endY - startY;
                        //更新浮窗位置
                        params.x += dx;
                        params.y += dy;
                        //防止坐标偏移屏幕
                        if (params.x<0){
                            params.x = 0;
                        }
                        if (params.y<0){
                            params.y = 0;
                        }
                        if (params.x>winWidth - view.getWidth()){
                            params.x = winWidth - view.getWidth();
                        }
                        if (params.y>winHeight - view.getHeight()){
                            params.y = winHeight  - view.getHeight();
                        }
                        windowManager.updateViewLayout(view,params);

                        //重新初始化坐标
                        startY = (int) event.getRawY();
                        startX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                        editor.putInt("lastX",params.x);
                        editor.putInt("lastY",params.y);
                        editor.apply();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);  //停止来电监听
        unregisterReceiver(receiver);  //注销广播
    }
}
