package com.yan.mobilesafe.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.yan.mobilesafe.Bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单数据库
 * Created by a7501 on 2015/12/5.
 */
public class BlackNumberDb {

    private static final String TABLENAME = "blacknumber";

    private final BlackNumberDbHelper helper;
    private String mode;

    public BlackNumberDb(Context context) {
        helper = new BlackNumberDbHelper(context);
    }

    /**
     * 添加号码
     *
     * @param number 号码
     * @param mode   模式
     * @return 状态
     */
    public boolean addNumber(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);
        long rowid = db.insert(TABLENAME, null, contentValues);
        if (rowid == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除号码
     *
     * @param number 号码
     * @return 状态
     */
    public boolean deleteNumber(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete(TABLENAME, "number = ?", new String[]{number});
        if (delete == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 改变拦截模式
     *
     * @param number 号码
     * @param mode   模式
     * @return 状态
     */
    public boolean changeNumberMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        int update = db.update(TABLENAME, contentValues, "number = ?", new String[]{number});
        return update != 0;
    }

    /**
     * 查找号码的拦截模式
     *
     * @param number 号码
     * @return 模式
     */
    public String findNumber(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(TABLENAME, new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 查询所有黑名单
     * @return
     */
    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<BlackNumberInfo> blackNumberInfoList = new ArrayList<BlackNumberInfo>();
        Cursor cursor = db.query(TABLENAME, new String[]{"number", "mode"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            blackNumberInfo.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            blackNumberInfoList.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(2000);
        return blackNumberInfoList;
    }
}
