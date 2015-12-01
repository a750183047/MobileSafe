package com.yan.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.yan.mobilesafe.DataBase.AddressDB;
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
    private TextView textView;

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
                    if (windowManager != null  ){
                        windowManager.removeView(textView);  //移除view
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

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");

        textView = new TextView(this);
        textView.setText(string);
        textView.setTextColor(Color.BLUE);
        windowManager.addView(textView,params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);  //停止来电监听
        unregisterReceiver(receiver);  //注销广播
    }
}
