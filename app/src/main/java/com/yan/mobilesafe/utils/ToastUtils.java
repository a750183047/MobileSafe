package com.yan.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出提示
 * Created by a7501 on 2015/11/24.
 */
public class ToastUtils {
    public static void showToast(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }
}
