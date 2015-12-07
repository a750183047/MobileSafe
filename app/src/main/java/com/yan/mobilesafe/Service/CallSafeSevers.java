package com.yan.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.yan.mobilesafe.DataBase.BlackNumberDb;



public class CallSafeSevers extends Service {

    private BlackNumberDb db;

    public CallSafeSevers() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new BlackNumberDb(this);
        //初始化短信的广播

        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(innerReceiver,intentFilter);

    }

    private class InnerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objectses = (Object[]) intent.getExtras().get("pdus");
            for (Object object:objectses) {
                SmsMessage message;
                message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();  //获取短信来源号码
                String messageBody = message.getMessageBody(); //获取短信内容
                String mode  = db.findNumber(originatingAddress);

                if (mode.equals("1")){
                    abortBroadcast();
                }else if (mode.equals("3")){
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
