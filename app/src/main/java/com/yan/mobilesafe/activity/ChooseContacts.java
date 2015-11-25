package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yan.mobilesafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择联系人界面
 * Created by a7501 on 2015/11/24.
 */
public class ChooseContacts extends AppCompatActivity {
    ListView contactsView;
    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<String>();
    private String name;
    private String number;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_contacts_layout);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        contactsView = (ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsView.setAdapter(adapter);
        readContacts();

        contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chickNumber = adapter.getItem(position);
                chickNumber = chickNumber.substring(chickNumber.indexOf("\n") + 1, chickNumber.length());

                Intent intent = new Intent();
                intent.putExtra("phoneNumber", chickNumber);
                setResult(Activity.RESULT_OK, intent);   //回调返回手机号码
                finish();
            }
        });
    }

    private void readContacts() {
        Cursor cursor = null;
        cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        assert cursor != null;
        while (cursor.moveToNext()) {
            //获取联系人名字
            name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            ));
            //获取联系人手机号
            number = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            ));
            contactsList.add(name + "\n" + number);
        }
        cursor.close();
    }
}
