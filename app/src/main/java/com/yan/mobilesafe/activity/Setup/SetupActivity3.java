package com.yan.mobilesafe.activity.Setup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.activity.ChooseContacts;
import com.yan.mobilesafe.utils.ToastUtils;


/**
 * 第三个设置向导页
 * Created by yan on 2015/11/22.
 */
public class SetupActivity3 extends AppCompatActivity {

    private EditText inputNumber;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        Button next = (Button) findViewById(R.id.next);
        Button back = (Button) findViewById(R.id.back);
        Button chooseContacts = (Button) findViewById(R.id.choose_contacts);
        inputNumber = (EditText) findViewById(R.id.et_number_input);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        String safePhone = sharedPreferences.getString("safe_number","");
        inputNumber.setText(safePhone);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = inputNumber.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    ToastUtils.showToast(SetupActivity3.this, "安全号码不能为空");
                    return;
                }

                sharedPreferences.edit().putString("safe_number", number).apply();//保存安全号码

                startActivity(new Intent(getApplicationContext(), SetupActivity4.class));
                finish();
                overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SetupActivity2.class));
                finish();
                // 两个界面切换的动画
                overridePendingTransition(R.anim.tran_previous_in,
                        R.anim.tran_previous_out);// 进入动画和退出动画
            }
        });

        chooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity3.this, ChooseContacts.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String phoneNumber = data.getStringExtra("phoneNumber");
            inputNumber.setText(phoneNumber);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
