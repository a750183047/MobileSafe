package com.yan.mobilesafe.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.yan.mobilesafe.DataBase.BlackNumberDb;

import java.lang.reflect.Method;


public class CallSafeSevers extends Service {

    private BlackNumberDb db;
    private TelephonyManager telephonyMananger;

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

        //获取系统的电话服务
        telephonyMananger = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
        telephonyMananger.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);  //设置监听器


        //初始化短信的广播
        InnerReceiver innerReceiver = new InnerReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(innerReceiver, intentFilter);

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
//            * @see TelephonyManager#CALL_STATE_IDLE  电话闲置
//            * @see TelephonyManager#CALL_STATE_RINGING 电话铃响的状态
//            * @see TelephonyManager#CALL_STATE_OFFHOOK 电话接通
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: //响铃状态
                    String mode = db.findNumber(incomingNumber);
                    if (mode.equals("1") || mode.equals("2")) {

                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri,true,new MyContentObserver(new Handler(),incomingNumber));

                        //挂断电话
                        endCall();
                    }
                    break;
            }
        }
    }

    private class MyContentObserver extends ContentObserver{

        private String incomingNumber;
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler,String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        //当数据改变的时候调用的方法
        @Override
        public void onChange(boolean selfChange) {
            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber);
            super.onChange(selfChange);
        }
    }

    private void deleteCallLog(String incomingNumber) {
        Uri uri = Uri.parse("content://call_log/calls");
        getContentResolver().delete(uri,"number = ?",new String[]{incomingNumber});

    }

    /**
     * 挂断电话
     */
    private void endCall() {
        try {
            //通过类加载器加载ServiceManager
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前的方法
            Method method = clazz.getDeclaredMethod("getService", String.class);

            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);

            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objectses = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objectses) {
                SmsMessage message;
                message = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = message.getOriginatingAddress();  //获取短信来源号码
                String messageBody = message.getMessageBody(); //获取短信内容
                String mode = db.findNumber(originatingAddress);

                if (mode.equals("1")) {
                    abortBroadcast();
                } else if (mode.equals("3")) {
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
