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

import java.util.List;

/**
 * AppManagerAdapter
 * Created by a7501 on 2015/12/12.
 */
public class AppManagerHeadersAdapter extends RecyclerView.Adapter<AppManagerHeadersAdapter.SimpleViewHolder>
        implements StickyRecyclerHeadersAdapter<AppManagerHeadersAdapter.SimpleViewHolder> {
    private Context context;
    private List<AppInfo> appInfoList;

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_manager, parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        String appName = appInfoList.get(position).getApkName();
        Drawable appIcon = appInfoList.get(position).getIcon();
        long apkSize = appInfoList.get(position).getApkSize();
        String apkPackageName = appInfoList.get(position).getApkPackageName();

        holder.appIcon.setImageDrawable(appIcon);
        holder.appName.setText(appName);
        holder.appSize.setText(Formatter.formatFileSize(context,apkSize));
        if (appInfoList.get(position).isRom()){
            holder.appLocation.setText("手机内存");
        }else {
            holder.appLocation.setText("外部内存");
        }


    }

    @Override
    public long getHeaderId(int position) {
        return 0;
    }

    @Override
    public SimpleViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(SimpleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return appInfoList.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        private final ImageView appIcon;
        private final TextView appName;
        private final TextView appLocation;
        private final TextView appSize;
        private final Button appDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appLocation = (TextView) itemView.findViewById(R.id.app_location);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            appDelete = (Button) itemView.findViewById(R.id.app_delete);
        }
    }

    public AppManagerHeadersAdapter(Context context, List<AppInfo> appInfoList) {
        this.context = context;
        this.appInfoList = appInfoList;
    }
}
