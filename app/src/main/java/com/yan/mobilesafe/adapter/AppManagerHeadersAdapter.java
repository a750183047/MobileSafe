package com.yan.mobilesafe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
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


import java.util.List;

/**
 * AppManagerAdapter
 * Created by a7501 on 2015/12/12.
 */
public class AppManagerHeadersAdapter extends RecyclerView.Adapter<AppManagerHeadersAdapter.SimpleViewHolder>
        implements StickyRecyclerHeadersAdapter<AppManagerHeadersAdapter.ViewHeadersHolder> {
    private Context context;
    private List<AppInfo> appInfoList;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private String appName;
    private Drawable appIcon;
    private long apkSize;
    private String apkPackageName;
    private MyItemClickListener listener;
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_manager, parent, false);
        return new SimpleViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        AppInfo appInfo;
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
                appInfo = userAppInfos.get(position);
            } else {
                int lo = userAppInfos.size();
                appInfo = systemAppInfos.get(position - lo);
            }
            appName = appInfo.getApkName();
            appIcon = appInfo.getIcon();
            apkSize = appInfo.getApkSize();
            apkPackageName = appInfo.getApkPackageName();
            if (appInfo.isRom()) {
                holder.appLocation.setText("手机内存");
            } else {
                holder.appLocation.setText("外部内存");
            }
        }

        holder.appIcon.setImageDrawable(appIcon);
        holder.appName.setText(appName);
        holder.appSize.setText(Formatter.formatFileSize(context, apkSize));




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
            holder.tt.setText("用户程序(" + userAppInfos.size()+1 + ")");
        } else if (position == userAppInfos.size() + 1) {
            holder.tt.setText("系统程序(" + systemAppInfos.size()+1 + ")");
        }


    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.listener = listener;
    }
}
