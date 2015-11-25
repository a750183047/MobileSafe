package com.yan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.Service.LocationService;

/**
 * 短信拦截
 * Created by a7501 on 2015/11/25.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        assert objects != null;
        for (Object object :objects){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = message.getOriginatingAddress();  //获取短信来源号码
            String messageBody = message.getMessageBody(); //获取短信内容
            abortBroadcast();// 中断短信的传递, 从而系统短信app就收不到内容了
            Log.e("SmsReceiver",originatingAddress + ":" +messageBody);

            if ("#*alarm*#".equals(messageBody)){
                //播放报警音乐
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setVolume(1f,1f);
                player.setLooping(true);
                player.start();
            }else if ("#*location*#".equals(messageBody)) {

                context.startService(new Intent(context, LocationService.class));
                String location = sharedPreferences.getString("location","get location");
                Log.e("SmsReceiver","获取经纬度" +location);

            }

        }
    }
}
