package com.example.xin.dormitory.houseparent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xin.dormitory.R;

import java.util.List;

public class StayAdapter extends RecyclerView.Adapter<StayAdapter.ViewHolder>  {

    private Context mContext;
    private List<Stay> mStayList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View stayView;
        //这是提交编号
        TextView tv_theID;
        TextView tv_stuID;
        TextView tv_dormID;
        TextView tv_registerDate;

        public ViewHolder(View view){
            super(view);
            stayView = view;
            tv_theID = view.findViewById(R.id.tv_theID);
            tv_registerDate = view.findViewById(R.id.tv_registerDate);
            tv_dormID = view.findViewById(R.id.tv_dormID);
            tv_stuID = view.findViewById(R.id.tv_stuID);
        }
    }

    public StayAdapter(List<Stay> stayList){
        mStayList = stayList;
    }

    @Override
    public StayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.stay_and_depart,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.stayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Stay stay = mStayList.get(position);
                View dialogView = LayoutInflater.from(mContext)
                        .inflate(R.layout.stay_student_details, null);
                TextView tv_dormID = dialogView.findViewById(R.id.tv_dormID);
                TextView tv_ID = dialogView.findViewById(R.id.tv_ID);
                TextView tv_name = dialogView.findViewById(R.id.tv_name);
                TextView tv_contact = dialogView.findViewById(R.id.tv_contact);
                TextView tv_startDate = dialogView.findViewById(R.id.tv_startDate);
                TextView tv_endDate = dialogView.findViewById(R.id.tv_endDate);
                TextView tv_registerDate = dialogView.findViewById(R.id.tv_registerDate);

                tv_dormID.setText(stay.getDormID());
                tv_ID.setText(stay.getID());
                tv_name.setText(stay.getName());
                tv_contact.setText(stay.getContact());
                tv_startDate.setText(stay.getStartDate());
                tv_endDate.setText(stay.getEndDate());
                tv_registerDate.setText(stay.getRegisterDate());

                new AlertDialog.Builder(mContext).setTitle("留宿学生详情")
                        .setView(dialogView)
                        .setNegativeButton("关闭", null).show();
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(StayAdapter.ViewHolder holder, int position) {
        Stay stay = mStayList.get(position);
        holder.tv_registerDate.setText(stay.getRegisterDate());
        holder.tv_theID.setText("编号:"+stay.getStayID());
        holder.tv_stuID.setText(stay.getID());
        holder.tv_dormID.setText(stay.getDormID());
    }

    @Override
    public int getItemCount() {
        return mStayList.size();
    }

}
