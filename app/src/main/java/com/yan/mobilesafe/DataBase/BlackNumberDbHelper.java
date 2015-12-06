package com.yan.mobilesafe.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * Created by a7501 on 2015/12/5.
 */
public class BlackNumberDbHelper extends SQLiteOpenHelper {
    public BlackNumberDbHelper(Context context) {
        super(context, "blackNumber.db", null, 1);
    }

    /**
     * 创建数据库
     * _id 自增主键
     * number 手机号码
     * mode 拦截模式
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber " +
                "(_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
