package com.yan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 监听手机开机启动广播
 * Created by a7501 on 2015/11/24.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protect = sharedPreferences.getBoolean("protect", false);
        //只有在防盗保护开启的前提下才进行sim卡判断
        if (protect) {
            String simNumber = sharedPreferences.getString("sim", null);
            if (!TextUtils.isEmpty(simNumber)) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentSimNumber = telephonyManager.getSimSerialNumber();
                if (currentSimNumber.equals(simNumber)) {
                    Log.e("BootReceiver", "手机安全");
                } else {
                    Log.e("BootReceiver", "手机危险");
                    String phone = sharedPreferences.getString("safe_number", "");
                    //发送短信给安全号码
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, "sim card changed", null, null);
                }
            }
        }


    }
}
