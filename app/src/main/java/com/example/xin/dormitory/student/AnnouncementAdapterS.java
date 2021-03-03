package com.example.xin.dormitory.student;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.xin.dormitory.common.Announcement;
import com.example.xin.dormitory.R;

import java.util.List;

public class AnnouncementAdapterS extends RecyclerView.Adapter<AnnouncementAdapterS.ViewHolder> {

    private Context mContext;
    private List<Announcement> mAnnouncementList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View announcementView;
        //这是发布编号
        TextView tv_theID;
        TextView tv_title;
        TextView tv_Atime;
        Button check_announcement;
        public ViewHolder(View view){
            super(view);
            announcementView = view;
//            tv_theID = view.findViewById(R.id.tv_theID);
            tv_title = view.findViewById(R.id.tv_title);
            tv_Atime = view.findViewById(R.id.tv_Atime);
            check_announcement=view.findViewById(R.id.check_announcement);
        }
    }

    public AnnouncementAdapterS(List<Announcement> announcementList){
        mAnnouncementList = announcementList;
    }

    @Override
    public AnnouncementAdapterS.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        //announcement布局重用了
        View view = LayoutInflater.from(mContext).inflate(R.layout.announcement_s,parent,false);
        final AnnouncementAdapterS.ViewHolder holder = new AnnouncementAdapterS.ViewHolder(view);
        holder.check_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Announcement announcement = mAnnouncementList.get(position);
                //此处的intent不同于宿管端，而两端共用了announcement布局，故另建此类
                Intent intent = new Intent(view.getContext(),CheckAnnouncementDetailsActivity.class);
                intent.putExtra("announcement_data",announcement);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(AnnouncementAdapterS.ViewHolder holder, int position) {
        Announcement announcement = mAnnouncementList.get(position);
//        holder.tv_theID.setText("编号:"+announcement.getID());
        holder.tv_title.setText(announcement.getTitle());
        holder.tv_Atime.setText(announcement.getAtime());
    }

    @Override
    public int getItemCount() {
        return mAnnouncementList.size();
    }
}
