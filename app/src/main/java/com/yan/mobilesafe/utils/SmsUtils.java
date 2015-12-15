package com.yan.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 短信备份工具
 * Created by a7501 on 2015/12/15.
 */
public class SmsUtils {

    public static boolean backupSms(Context context) {

        //内容提供者
        ContentResolver contentResolver = context.getContentResolver();
        Uri parse = Uri.parse("content://sms/");//短信的地址
        Cursor cursor = contentResolver.query
                (parse, new String[]{"address", "date", "type", "body"}, null, null, null);

        try {
            File backupSMS = new File(Environment.getExternalStorageDirectory() + "/MobileSafe/", "backupSMS.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(backupSMS);
            //得到序列化器
            XmlSerializer xmlSerializer = Xml.newSerializer();
            //把短信序列化到sd卡然后设置编码格式
            xmlSerializer.setOutput(fileOutputStream, "utf-8");
            //
            xmlSerializer.startDocument("utf-8", true);
            //设置开始节点，第一个参数是命名空间。第二个参数节点的名字
            xmlSerializer.startTag(null, "smss");
            //设置smss节点上面的属性，第二个参数是名字，第三个参数是值
            // xmlSerializer.attribute(null,"sms",)
            //移动游标
            assert cursor != null;
            while (cursor.moveToNext()) {
                xmlSerializer.startTag(null, "sms");
                    xmlSerializer.startTag(null, "address");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("address")));
                     xmlSerializer.endTag(null, "address");
                    xmlSerializer.startTag(null, "date");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("date")));
                    xmlSerializer.endTag(null, "date");
                    xmlSerializer.startTag(null, "type");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("type")));
                    xmlSerializer.endTag(null, "type");
                    xmlSerializer.startTag(null, "body");
                        xmlSerializer.text(cursor.getString(cursor.getColumnIndex("body")));
                    xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null,"sms");

            }
            cursor.close();
            xmlSerializer.endDocument();
            fileOutputStream.flush();
            fileOutputStream.close();
            xmlSerializer.endTag(null, "smss");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
