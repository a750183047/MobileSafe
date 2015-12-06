package com.yan.mobilesafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yan.mobilesafe.Bean.BlackNumberInfo;
import com.yan.mobilesafe.R;

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
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        String phoneName = blackNumberInfo.get(position).getNumber();
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
                Log.e("MyAdapter", "delete" + position);
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
