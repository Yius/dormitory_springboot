package com.example.xin.dormitory.houseparent;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 此类将被HandledRepairActivity和UnhandledRepairActivity共用
 */
public class RepairAdapter extends RecyclerView.Adapter<RepairAdapter.ViewHolder> {

    private Context mContext;
    private List<Repair> mRepairList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View repairView;
        TextView tv_applyID;
        TextView tv_applyDate;
        TextView tv_dormID;
        TextView tv_repairName;
        TextView tv_status;

        public ViewHolder(View view){
            super(view);
            repairView = view;
            tv_applyDate = view.findViewById(R.id.tv_applyDate);
            tv_applyID = view.findViewById(R.id.tv_applyID);
            tv_dormID = view.findViewById(R.id.tv_dormID);
            tv_repairName = view.findViewById(R.id.tv_repairName);
            tv_status = view.findViewById(R.id.tv_status);
        }
    }

    public RepairAdapter(List<Repair> repairList){
        mRepairList = repairList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.repair,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.repairView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                final Repair repair = mRepairList.get(position);
//                    Intent intent = new Intent(view.getContext(),RepairDetailsActivity.class);
//                    intent.putExtra("repair_data",repair);
//                    mContext.startActivity(intent);
                //此处不能用MyApplication.getContext()
                View dialogView = LayoutInflater.from(mContext)
                        .inflate(R.layout.repair_details, null);
                TextView tv_dormIDDetail = dialogView.findViewById(R.id.tv_dormIDDetail);
                TextView tv_RepairName = dialogView.findViewById(R.id.tv_RepairName);
                TextView tv_DamageCause = dialogView.findViewById(R.id.tv_DamageCause);
                TextView tv_Details = dialogView.findViewById(R.id.tv_Details);
                TextView tv_Contact = dialogView.findViewById(R.id.tv_Contact);
                TextView tv_OtherRemarks = dialogView.findViewById(R.id.tv_OtherRemarks);

                tv_dormIDDetail.setText(repair.getDormID());
                tv_RepairName.setText(repair.getRepairName());
                tv_DamageCause.setText(repair.getDamageCause());
                tv_Details.setText(repair.getDetails());
                tv_Contact.setText(repair.getContact());
                tv_OtherRemarks.setText(repair.getOtherRemarks());

                tv_RepairName.setMovementMethod(ScrollingMovementMethod.getInstance());
                tv_DamageCause.setMovementMethod(ScrollingMovementMethod.getInstance());
                tv_Details.setMovementMethod(ScrollingMovementMethod.getInstance());
                tv_OtherRemarks.setMovementMethod(ScrollingMovementMethod.getInstance());

                if (repair.getStatus() == 0) {
                    new AlertDialog.Builder(mContext).setTitle("申请详情")
                            .setView(dialogView)
                            .setPositiveButton("标为已处理", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //网络操作耗时，故开子线程
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            OkHttpClient client = new OkHttpClient();
                                            RequestBody requestBody = new FormBody.Builder().add("ApplyID", repair.getApplyID() + "").build();
                                            //服务器地址，ip地址需要时常更换
                                            String address = HttpUtil.address + "alterRepairInfo.php";
                                            Request request = new Request.Builder().url(address).post(requestBody).build();
                                            //匿名内部类实现回调接口
                                            client.newCall(request).enqueue(new okhttp3.Callback() {

                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    e.printStackTrace();
                                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(MyApplication.getContext(), "服务器连接失败，无法修改", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    String responseData = response.body().string();
                                                    if (HttpUtil.parseSimpleJSONData(responseData)) {
                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(MyApplication.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(MyApplication.getContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }
                                    }).start();

                                }

                            }).setNegativeButton("关闭", null).show();
                }else {
                    new AlertDialog.Builder(mContext).setTitle("申请详情")
                            .setView(dialogView)
                            .setNegativeButton("关闭", null).show();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repair repair = mRepairList.get(position);
        holder.tv_repairName.setText(repair.getRepairName());
        holder.tv_dormID.setText(repair.getDormID());
        //特别注意！传入int会闪退！
        holder.tv_applyID.setText("编号:"+repair.getApplyID());
        holder.tv_applyDate.setText(repair.getApplyDate());
        if(repair.getStatus()==1){
            holder.tv_status.setTextColor(Color.rgb(211,211,211));
            holder.tv_status.setText("已处理");
        }else{
            //别设置成0xFF4500这样，透明
            holder.tv_status.setTextColor(Color.rgb(255,69,0));
            holder.tv_status.setText("未处理");
        }
    }

    @Override
    public int getItemCount() {
        return mRepairList.size();
    }
}
