package com.yan.mobilesafe.activity.Atool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_layout);

        inputNumber = (EditText) findViewById(R.id.et_number_address);
        select = (Button) findViewById(R.id.btn_ok);
        result = (TextView) findViewById(R.id.tv_result);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(inputNumber.getText().toString())){
                    result.setText(AddressDB.getAddress(inputNumber.getText().toString()));
                }
            }
        });

    }
}
