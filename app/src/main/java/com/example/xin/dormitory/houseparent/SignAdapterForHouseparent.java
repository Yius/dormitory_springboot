package com.example.xin.dormitory.houseparent;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.example.xin.dormitory.common.Announcement;
import com.example.xin.dormitory.common.Sign;
import com.example.xin.dormitory.R;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//未完成
public class SignAdapterForHouseparent extends RecyclerView.Adapter<SignAdapterForHouseparent.ViewHolder> {

    private Context mContext;
    private List<Sign> mSignList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View signView;
        //这是发布编号
        TextView tv_theID;
        TextView tv_Rtime;
        TextView tv_nums;
        TextView tv_title;
        TextView tv_totalnums;

        public ViewHolder(View view) {
            super(view);
            signView = view;
            tv_theID = view.findViewById(R.id.tv_theID);
            tv_Rtime = view.findViewById(R.id.tv_Rtime);
            tv_nums = view.findViewById(R.id.tv_nums);
            tv_title = view.findViewById(R.id.tv_title);
            tv_totalnums = view.findViewById(R.id.tv_totalnums);
        }
    }

    public SignAdapterForHouseparent(List<Sign> signList) {
        mSignList = signList;
    }

    @Override
    public SignAdapterForHouseparent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.sign_for_houseparent, parent, false);
        final SignAdapterForHouseparent.ViewHolder holder = new SignAdapterForHouseparent.ViewHolder(view);
        holder.signView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Sign sign = mSignList.get(position);
                Intent intent = new Intent(view.getContext(), CheckUnsignedStudentsActivity.class);
                intent.putExtra("sign_data", sign);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SignAdapterForHouseparent.ViewHolder holder, int position) {
        Sign sign = mSignList.get(position);
        holder.tv_Rtime.setText("发布时间:" + sign.getRtime());
        holder.tv_title.setText("标题:" + sign.getTitle());
        holder.tv_theID.setText("编号:" + sign.getID());
        holder.tv_nums.setText("签到人数:" + sign.getNums());
        holder.tv_totalnums.setText("应签到人数:" + sign.getTotalnums());

        holder.signView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new MaterialDialog.Builder(mContext)
                        .content("确定要删除这条签到记录吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = new FormBody.Builder().add("ID", String.valueOf(sign.getID())).build();
                                //服务器地址，ip地址需要时常更换
                                String address = HttpUtil.address + "deleteSign.php";
                                Request request = new Request.Builder().url(address).post(requestBody).build();
                                //匿名内部类实现回调接口
                                client.newCall(request).enqueue(new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MyApplication.getContext(), "服务器连接失败", Toast.LENGTH_SHORT).show();
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
                                                    mSignList.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, mSignList.size() - position);//更新适配器这条后面列表的变化
                                                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MyApplication.getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        })
                        .show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSignList.size();
    }

}
