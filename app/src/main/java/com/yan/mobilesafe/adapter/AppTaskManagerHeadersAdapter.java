package com.yan.mobilesafe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.yan.mobilesafe.Bean.TaskInfo;
import com.yan.mobilesafe.R;
import com.yan.mobilesafe.adapter.listentr.MyItemClickListener;

import java.util.List;

/**
 * 软件内存信息adapter
 * Created by a7501 on 2015/12/21.
 */
public class AppTaskManagerHeadersAdapter extends
        RecyclerView.Adapter<AppTaskManagerHeadersAdapter.SimpleViewHolder> implements
        StickyRecyclerHeadersAdapter<AppTaskManagerHeadersAdapter.ViewHeadersHolder> {

    private Context context;
    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userInfos;
    private List<TaskInfo> systemInfos;
    private String appName;
    private Drawable icon;
    private String appSize;
    private MyItemClickListener listener;
    private boolean isChicked;


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //   View inflate = View.inflate(context, R.layout.item_task_manager,parent); //如果在fragment中使用这个获取view 会出现异常
        // 详见 http://www.cnblogs.com/over140/archive/2013/06/06/3121354.html
        View inflates = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_task_manager, parent, false); //使用这个方法可以避免
        // The specified child already has a parent. You must call removeView() on the child's parent first. 这个异常
        return new SimpleViewHolder(inflates,listener);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        TaskInfo taskInfo;
        if (position<userInfos.size()){
            taskInfo = userInfos.get(position);
            appName = taskInfo.getAppName();
            icon = taskInfo.getDrawable();
            appSize = Formatter.formatFileSize(context, taskInfo.getMemorySize());
            isChicked = taskInfo.isChickde();
        }else {
            int po = userInfos.size();
            taskInfo = systemInfos.get(position-po);
            appName = taskInfo.getAppName();
            icon = taskInfo.getDrawable();
            appSize = Formatter.formatFileSize(context, taskInfo.getMemorySize());
            isChicked = taskInfo.isChickde();
        }

        if (isChicked){
            holder.cbAppTesk.setChecked(true);
        }else {
            holder.cbAppTesk.setChecked(false);
        }
        holder.appIcon.setImageDrawable(icon);
        holder.appName.setText(appName);
        holder.appSize.setText(appSize);

    }

    @Override
    public long getHeaderId(int position) {
        if (position <= userInfos.size()) {
            return 0;
        } else if (position > userInfos.size()) {
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
            holder.tt.setText("用户程序");
        } else if (position == userInfos.size() + 1) {
            holder.tt.setText("系统程序");
        }
    }


    @Override
    public int getItemCount() {
        return userInfos.size()+systemInfos.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView appIcon;
        private final TextView appName;
        private final TextView appSize;
        public final CheckBox cbAppTesk;
        private MyItemClickListener listener;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            cbAppTesk = (CheckBox) itemView.findViewById(R.id.cb_app_task);
        }

        public SimpleViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            cbAppTesk = (CheckBox) itemView.findViewById(R.id.cb_app_task);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getPosition());
            }
        }
    }

    public class ViewHeadersHolder extends RecyclerView.ViewHolder {

        private final TextView tt;

        public ViewHeadersHolder(View itemView) {
            super(itemView);
            tt = (TextView) itemView.findViewById(R.id.app_list_header);
        }
    }

    public AppTaskManagerHeadersAdapter(Context context, List<TaskInfo> taskInfos) {
        this.context = context;
        this.taskInfos = taskInfos;
    }

    public AppTaskManagerHeadersAdapter(Context context, List<TaskInfo> userInfos,List<TaskInfo> systemInfos) {
        this.context = context;
        this.userInfos = userInfos;
        this.systemInfos = systemInfos;
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.listener = listener;
    }

}
