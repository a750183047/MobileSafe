package com.yan.mobilesafe.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yan.mobilesafe.Bean.BlackNumberInfo;
import com.yan.mobilesafe.DataBase.BlackNumberDb;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter
 * Created by a7501 on 2015/12/6.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.SimpleViewHolder> {

    Context context;
    List<BlackNumberInfo> blackNumberInfo;

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_adapter, parent, false);

        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        final String phoneName = blackNumberInfo.get(position).getNumber();
        String phoneMode = blackNumberInfo.get(position).getMode();
        String mode = null;

        if (phoneMode.equals("1")) {
            mode = "短信拦截";
        } else if (phoneMode.equals("2")) {
            mode = "电话拦截";
        } else if (phoneMode.equals("3")) {
            mode = "双重拦截";
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建一个弹窗 警告是否要删除数据
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("确认删除该号码吗");
                builder.setMessage("你确认要删除 " + phoneName + "的号码吗？该操作无法恢复！");
                builder.setCancelable(false);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //数据库操作
                        BlackNumberDb blackNumberDb = new BlackNumberDb(context);
                        boolean b = blackNumberDb.deleteNumber(phoneName);
                        if (b){

                        }else {
                            ToastUtils.showToast(context,"糟糕，删除失败了，再试一下");
                        }
                        //这里是个坑  http://blog.csdn.net/wangkai0681080/article/details/50082825
                        blackNumberInfo.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        holder.phoneNumber.setText(phoneName);
        holder.phoneMode.setText(mode);

    }


    @Override
    public int getItemCount() {
        return blackNumberInfo.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        private TextView phoneNumber;
        private TextView phoneMode;
        private ImageView delete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            phoneNumber = (TextView) itemView.findViewById(R.id.tv_phone_number);
            phoneMode = (TextView) itemView.findViewById(R.id.tv_mode);
            delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("MyAdapter", "phone" + phoneNumber.getText());
                }
            });
        }
    }

    public MyAdapter(Context context, List<BlackNumberInfo> blackNumberInfos) {
        this.context = context;
        this.blackNumberInfo = blackNumberInfos;
    }
}
