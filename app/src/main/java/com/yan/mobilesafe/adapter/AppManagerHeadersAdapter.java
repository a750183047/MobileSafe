package com.yan.mobilesafe.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.yan.mobilesafe.Bean.AppInfo;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.listentr.MyItemClickListener;
import com.yan.mobilesafe.utils.ToastUtils;


import java.util.List;

/**
 * AppManagerAdapter
 * Created by a7501 on 2015/12/12.
 */
public class AppManagerHeadersAdapter extends RecyclerView.Adapter<AppManagerHeadersAdapter.SimpleViewHolder>
        implements StickyRecyclerHeadersAdapter<AppManagerHeadersAdapter.ViewHeadersHolder> {
    private Context context;
    private List<AppInfo> appInfoList = null;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private String appName;
    private Drawable appIcon;
    private long apkSize;
    private String apkPackageName;
    private MyItemClickListener listener;
    private Intent uninstall_localIntent;


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_manager, parent, false);
        return new SimpleViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {

        if (appInfoList != null) {
            appName = appInfoList.get(position).getApkName();
            appIcon = appInfoList.get(position).getIcon();
            apkSize = appInfoList.get(position).getApkSize();
            apkPackageName = appInfoList.get(position).getApkPackageName();
            if (appInfoList.get(position).isRom()) {
                holder.appLocation.setText("手机内存");
            } else {
                holder.appLocation.setText("外部内存");
            }
        } else {

            if (position < userAppInfos.size()) {
                appName = userAppInfos.get(position).getApkName();
                appIcon = userAppInfos.get(position).getIcon();
                apkSize = userAppInfos.get(position).getApkSize();
                apkPackageName = userAppInfos.get(position).getApkPackageName();
                if (userAppInfos.get(position).isRom()) {
                    holder.appLocation.setText("手机内存");
                } else {
                    holder.appLocation.setText("外部内存");
                }

            } else {
                int lo = userAppInfos.size();
                appName = systemAppInfos.get(position - lo).getApkName();
                appIcon = systemAppInfos.get(position - lo).getIcon();
                apkSize = systemAppInfos.get(position - lo).getApkSize();
                apkPackageName = systemAppInfos.get(position - lo).getApkPackageName();
                if (systemAppInfos.get(position - lo).isRom()) {
                    holder.appLocation.setText("手机内存");
                } else {
                    holder.appLocation.setText("外部内存");
                }
            }

        }

        holder.appIcon.setImageDrawable(appIcon);
        holder.appName.setText(appName);
        holder.appSize.setText(Formatter.formatFileSize(context, apkSize));
        //删除按钮监听
        holder.appDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < userAppInfos.size()){
                    uninstall_localIntent = new Intent("android.intent.action.DELETE",
                            Uri.parse("package:" + userAppInfos.get(position).getApkPackageName()));
                }else {
                    uninstall_localIntent = new Intent("android.intent.action.DELETE",
                            Uri.parse("package:" + systemAppInfos.get(position - userAppInfos.size()).getApkPackageName()));
                }
                context.startActivity(uninstall_localIntent);
               // notifyItemRemoved(holder.getAdapterPosition());
            }
        });




    }


    @Override
    public int getItemCount() {
        if (appInfoList != null) {
            return appInfoList.size();
        } else {
            return userAppInfos.size() + systemAppInfos.size();
        }

    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView appIcon;
        private final TextView appName;
        private final TextView appLocation;
        private final TextView appSize;
        private final Button appDelete;
        private MyItemClickListener listener;


        public SimpleViewHolder(View itemView)   {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appLocation = (TextView) itemView.findViewById(R.id.app_location);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            appDelete = (Button) itemView.findViewById(R.id.app_delete);
        }
        public SimpleViewHolder(View itemView,MyItemClickListener listener){
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appLocation = (TextView) itemView.findViewById(R.id.app_location);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            appDelete = (Button) itemView.findViewById(R.id.app_delete);
            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        /**
         * 点击监听接口实现
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (listener !=null){
                listener.onItemClick(v,getPosition());
            }
        }


    }

    public AppManagerHeadersAdapter(Context context, List<AppInfo> appInfoList) {
        this.context = context;
        this.appInfoList = appInfoList;
    }

    public AppManagerHeadersAdapter(Context context, List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos) {
        this.context = context;
        this.userAppInfos = userAppInfos;
        this.systemAppInfos = systemAppInfos;
    }

    /**
     * 一下为表头的样式
     */

    public class ViewHeadersHolder extends RecyclerView.ViewHolder {

        private final TextView tt;

        public ViewHeadersHolder(View itemView) {
            super(itemView);
            tt = (TextView) itemView.findViewById(R.id.app_list_header);
        }
    }


    @Override
    public long getHeaderId(int position) {
        if (position <= userAppInfos.size()) {
            return 0;
        } else if (position > userAppInfos.size()) {
            return 1;
        } else {
            return -1;
        }

    }

    @Override
    public ViewHeadersHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recycler_header, parent, false);
        return new ViewHeadersHolder(itemView);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHeadersHolder holder, int position) {

        if (position == 0) {
            holder.tt.setText("用户程序(" + (userAppInfos.size()+1) + ")");
        } else if (position == userAppInfos.size() + 1) {
            holder.tt.setText("系统程序(" + (systemAppInfos.size()+1) + ")");
        }


    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.listener = listener;
    }
}
