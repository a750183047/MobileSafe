package com.yan.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yan.mobilesafe.R;
import com.yan.mobilesafe.activity.Setup.SetupActivity1;
import com.yan.mobilesafe.utils.MD5utils;

/**
 * 手机防盗活动
 * Created by yan on 2015/11/22.
 */
public class SimGuard extends AppCompatActivity implements TextWatcher {

    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Button btnOk;
    private Button btnCancel;
    private String password;
    private String passwordConfirm;
    private View view;
    private View viewInput;
    private EditText passwordInput;
    private SharedPreferences sharedPreferences;
    private boolean configed;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //设置输入框监听

        //实例化各个控件
        view = View.inflate(this, R.layout.dailog_set_password, null);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
        etPassword.addTextChangedListener(this);
        etPasswordConfirm.addTextChangedListener(this);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        viewInput = View.inflate(this, R.layout.dailog_input_password, null);


        configed = sharedPreferences.getBoolean("configed", false);

        showPasswordDialog();


    }

    /**
     * 弹出输入密码框
     */
    private void showPasswordDialog() {
        //判断是否设置过密码
        String savePassword = sharedPreferences.getString("password", null);
        if (!TextUtils.isEmpty(savePassword)) {
            showPasswordInputDialog();
        } else {
            showPasswordSetDialog();
        }

    }

    /**
     * 弹出输入密码框
     */
    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setView(viewInput);
        dialog.setCancelable(false);

        Button btnOk = (Button) viewInput.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) viewInput.findViewById(R.id.btn_cancel);
        passwordInput = (EditText) viewInput.findViewById(R.id.et_password);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordInput.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    String savePassword = sharedPreferences.getString("password", null);
                    //校验密码
                    if (MD5utils.encode(password).equals(savePassword)) {
                        isFirstIn();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "输入框内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();

    }

    /**
     * 弹出设置密码框
     */
    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(view);  //把布局文件加载给Dialog

        //实例化button
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = etPassword.getText().toString();
                passwordConfirm = etPasswordConfirm.getText().toString();
                if (password.equals(passwordConfirm)) {
                    sharedPreferences.edit().putString("password", MD5utils.encode(password)).apply();
                    isFirstIn();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "两次密码不一致，请检查", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();


    }


    //输入框监听接口的实现
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(etPassword.getText().toString())
                && TextUtils.isEmpty(etPasswordConfirm.getText().toString())) {
            btnOk.setEnabled(false);
        } else {
            btnOk.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    /**
     * 判断是否进入引导页进入引导页
     */
    private void isFirstIn() {
        if (configed) {
            setContentView(R.layout.sim_guard_layout);
        } else {
            startActivity(new Intent(this, SetupActivity1.class));
            finish();
        }
    }
}
