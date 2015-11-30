package com.yan.mobilesafe.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yan.mobilesafe.DataBase.AddressDB;
import com.yan.mobilesafe.utils.ToastUtils;

/**
 * 监听电话服务
 * Created by a7501 on 2015/11/30.
 */
public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private MyListener listener;

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
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);  //监听来电状态

    }


    class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING : //电话响了
                    Log.e("AddressService","来电话了");
                    String address = AddressDB.getAddress(incomingNumber);
                    ToastUtils.showToast(getApplicationContext(),address);
                    break;

            }
            super.onCallStateChanged(state,incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);  //停止来电监听
    }
}
