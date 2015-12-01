package com.yan.mobilesafe.activity.Atool;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yan.mobilesafe.DataBase.AddressDB;
import com.yan.mobilesafe.R;

/**
 * 号码归属地查询
 * Created by a7501 on 2015/11/30.
 */
public class AddressActivity extends AppCompatActivity {

    private EditText inputNumber;
    private Button select;
    private TextView result;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_layout);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        inputNumber = (EditText) findViewById(R.id.et_number_address);
        select = (Button) findViewById(R.id.btn_ok);
        result = (TextView) findViewById(R.id.tv_result);

        inputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                result.setText(AddressDB.getAddress(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputNumber.getText().toString())) {
                    result.setText(AddressDB.getAddress(inputNumber.getText().toString()));
                }
            }
        });

    }
}
