package com.yan.mobilesafe.DataBase;

import android.database.sqlite.SQLiteDatabase;

/**
 * 导入归属地数据库
 * Created by a7501 on 2015/11/27.
 */
public class AddressDB {
    private static final String PATH = "data/data/com.yan.mobilesafe/files/address.db";

    public static String getAddress(String number){
        String address = "未知号码";

        //获取数据库对象
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH,null,SQLiteDatabase.OPEN_READONLY);

        return null;
    }
}
