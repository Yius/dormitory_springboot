package com.example.xin.dormitory.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.common.Sign;
import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class SignAdapterForStudent extends RecyclerView.Adapter<SignAdapterForStudent.ViewHolder>{

    private Context mContext;
    private List<Sign> mSignList;
    //为了获取方便定义为私有成员
    private Sign chosenSign;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View signView;
        //这是发布编号
        TextView tv_theID;
        TextView tv_Rtime;
        TextView tv_title;
        TextView tv_houseparentName;
        RoundButton sb_sign;

        public ViewHolder(View view){
            super(view);
            signView = view;
            tv_Rtime = view.findViewById(R.id.tv_Rtime);
            tv_title = view.findViewById(R.id.tv_title);
            tv_houseparentName = view.findViewById(R.id.tv_houseparentName);
            sb_sign = view.findViewById(R.id.sb_sign);
        }
    }

    public SignAdapterForStudent(List<Sign> signList){
        mSignList = signList;
    }

    @Override
    public SignAdapterForStudent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.sign_for_student,parent,false);
        final SignAdapterForStudent.ViewHolder holder = new SignAdapterForStudent.ViewHolder(view);
//        holder.tv_sign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//                chosenSign = mSignList.get(position);
//                new AlertDialog.Builder(mContext).setTitle("你要现在签到吗？")
//                        .setIcon(R.drawable.ic_sign_notice)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //按下确定键后的事件
//                                studentSignHelp(chosenSign);
//                            }
//                        }).setNegativeButton("取消",null).show();
//            }
//        });
        holder.sb_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                int position = holder.getAdapterPosition();
                Sign sign = mSignList.get(position);
                intent = new Intent(mContext, SignDetails.class);
                intent.putExtra("sign",sign);
                mContext.startActivity(intent);
                }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SignAdapterForStudent.ViewHolder holder, int position) {
        Sign sign = mSignList.get(position);
        holder.tv_Rtime.setText(sign.getRtime());
        holder.tv_title.setText(sign.getTitle());
//        holder.tv_theID.setText("编号:"+sign.getID());
        holder.tv_houseparentName.setText(sign.getHouseparentName());
    }

    @Override
    public int getItemCount() {
        return mSignList.size();
    }

    /**
     * 用于学生签到
     * @param sign 用于获取签到表编号
     */
    private void studentSignHelp(Sign sign){
        SharedPreferences pref = mContext.getSharedPreferences("data",MODE_PRIVATE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String signedtime = formatter.format(new Date(System.currentTimeMillis()));
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("signedtime",signedtime).add("SID",pref.getString("ID","")).add("recordID",String.valueOf(sign.getID())).build();
        //服务器地址，ip地址需要时常更换
        String address=HttpUtil.address+"signTimeRecord.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),"服务器连接失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                //子线程中操作Toast会出现问题，所以用runOnUiThread
                if (HttpUtil.parseSimpleJSONData(responseData)) {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "签到成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "签到失败,请不要重复签到哦", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
