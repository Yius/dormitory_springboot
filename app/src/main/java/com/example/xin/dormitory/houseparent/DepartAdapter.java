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

public class DepartAdapter extends RecyclerView.Adapter<DepartAdapter.ViewHolder>  {

    private Context mContext;
    private List<Depart> mDepartList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View departView;
        //这是提交编号
        TextView tv_theID;
        TextView tv_stuID;
        TextView tv_dormID;
        TextView tv_registerDate;

        public ViewHolder(View view){
            super(view);
            departView = view;
            tv_theID = view.findViewById(R.id.tv_theID);
            tv_registerDate = view.findViewById(R.id.tv_registerDate);
            tv_dormID = view.findViewById(R.id.tv_dormID);
            tv_stuID = view.findViewById(R.id.tv_stuID);
        }
    }

    public DepartAdapter(List<Depart> departList){
        mDepartList = departList;
    }

    @Override
    public DepartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.stay_and_depart,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.departView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Depart depart = mDepartList.get(position);
                View dialogView = LayoutInflater.from(mContext)
                        .inflate(R.layout.depart_student_details, null);
                TextView tv_dormID = dialogView.findViewById(R.id.tv_dormID);
                TextView tv_ID = dialogView.findViewById(R.id.tv_ID);
                TextView tv_name = dialogView.findViewById(R.id.tv_name);
                TextView tv_departCause = dialogView.findViewById(R.id.tv_departCause);
                TextView tv_contact = dialogView.findViewById(R.id.tv_contact);
                TextView tv_departTime = dialogView.findViewById(R.id.tv_departTime);
                TextView tv_backTime = dialogView.findViewById(R.id.tv_backTime);
                TextView tv_registerDate = dialogView.findViewById(R.id.tv_registerDate);

                tv_dormID.setText(depart.getDormID());
                tv_ID.setText(depart.getID());
                tv_name.setText(depart.getName());
                tv_departCause.setText(depart.getDepartCause());
                tv_contact.setText(depart.getContact());
                tv_departTime.setText(depart.getDepartTime());
                tv_backTime.setText(depart.getBackTime());
                tv_registerDate.setText(depart.getRegisterDate());

                new AlertDialog.Builder(mContext).setTitle("离宿学生详情")
                        .setView(dialogView)
                        .setNegativeButton("关闭", null).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DepartAdapter.ViewHolder holder, int position) {
        Depart depart = mDepartList.get(position);
        holder.tv_registerDate.setText(depart.getRegisterDate());
        holder.tv_theID.setText("编号:"+depart.getDepartID());
        holder.tv_stuID.setText(depart.getID());
        holder.tv_dormID.setText(depart.getDormID());
    }

    @Override
    public int getItemCount() {
        return mDepartList.size();
    }

}
