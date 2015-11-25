package com.yan.mobilesafe.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 获取位置服务
 * Created by a7501 on 2015/11/25.
 */
public class LocationService extends Service {

    private SharedPreferences sharedPreferences;
    private LocationManager locationManager;
    private MylocationListenter listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        //获取位置
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true); //是否获取付费
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true); //获取最佳位置提供者

        listener = new MylocationListenter();
        locationManager.requestLocationUpdates(bestProvider,0,0,listener);  //启动获取位置
    }

    class MylocationListenter implements LocationListener {

        //位置发生变化
        @Override
        public void onLocationChanged(Location location) {
            //保存经纬度
            sharedPreferences.edit().putString("location", "jing" +
                    location.getLongitude() + ";" + "wei" + location.getLatitude()).apply();

            stopSelf();   //停掉service

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
    }
}

