package com.example.xin.dormitory.houseparent;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xin.dormitory.R;

import java.util.List;


public class SimpleStudentInfoAdapter extends RecyclerView.Adapter<SimpleStudentInfoAdapter.ViewHolder>  {


    private Context mContext;
    private List<SimpleStudentInfo> mSimpleStudentInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View simpleStudentInfoView;
        //这是发布编号
        TextView tv_name;
        TextView tv_dormID;
        TextView tv_phone;

        public ViewHolder(View view){
            super(view);
            simpleStudentInfoView = view;
            tv_dormID = view.findViewById(R.id.tv_dormID);
            tv_name = view.findViewById(R.id.tv_name);
            tv_phone = view.findViewById(R.id.tv_phone);
        }
    }

    public SimpleStudentInfoAdapter(List<SimpleStudentInfo> simpleStudentInfoList){
        mSimpleStudentInfoList = simpleStudentInfoList;
    }

    @Override
    public SimpleStudentInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.simple_student_info,parent,false);
        final SimpleStudentInfoAdapter.ViewHolder holder = new SimpleStudentInfoAdapter.ViewHolder(view);
        holder.simpleStudentInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleStudentInfoAdapter.ViewHolder holder, int position) {
        SimpleStudentInfo simpleStudentInfo = mSimpleStudentInfoList.get(position);
        holder.tv_name.setText("姓名:"+simpleStudentInfo.getName());
        holder.tv_phone.setText("联系方式:"+simpleStudentInfo.getPhone());
        holder.tv_dormID.setText("宿舍号:"+simpleStudentInfo.getDormID());
    }

    @Override
    public int getItemCount() {
        return mSimpleStudentInfoList.size();
    }
    
}
