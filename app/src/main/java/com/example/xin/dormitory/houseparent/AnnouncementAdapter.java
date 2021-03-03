package com.example.xin.dormitory.houseparent;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.example.xin.dormitory.common.Announcement;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private Context mContext;
    private List<Announcement> mAnnouncementList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View announcementView;
        //这是发布编号
        TextView tv_theID;
        TextView tv_title;
        TextView tv_Atime;
        Button bt_delete;

        public ViewHolder(View view) {
            super(view);
            announcementView = view;
            tv_theID = view.findViewById(R.id.tv_theID);
            tv_title = view.findViewById(R.id.tv_title);
            tv_Atime = view.findViewById(R.id.tv_Atime);
            bt_delete = view.findViewById(R.id.bt_delete);
        }
    }

    public AnnouncementAdapter(List<Announcement> announcementList) {
        mAnnouncementList = announcementList;
    }

    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.announcement, parent, false);
        final AnnouncementAdapter.ViewHolder holder = new AnnouncementAdapter.ViewHolder(view);
        holder.announcementView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Announcement announcement = mAnnouncementList.get(position);
                //此处不能用MyApplication.getContext()
                View dialogView = LayoutInflater.from(mContext)
                        .inflate(R.layout.announcement_detail, null);
                TextView tv_title = dialogView.findViewById(R.id.tv_title);
                TextView tv_content = dialogView.findViewById(R.id.tv_content);
                TextView tv_atime = dialogView.findViewById(R.id.tv_atime);

                tv_title.setText(announcement.getTitle());
                tv_content.setText(announcement.getContent());
                tv_atime.setText(announcement.getAtime());


                tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());

                new AlertDialog.Builder(mContext).setTitle("通知详情")
                        .setView(dialogView)
                        .setNegativeButton("关闭", null).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(AnnouncementAdapter.ViewHolder holder, final int position) {
        final Announcement announcement = mAnnouncementList.get(position);
        holder.tv_title.setText(announcement.getTitle());
        holder.tv_theID.setText("编号:" + announcement.getID());
        holder.tv_Atime.setText(announcement.getAtime());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder().add("AID", String.valueOf(announcement.getID())).build();
                //服务器地址，ip地址需要时常更换
                String address = HttpUtil.address + "deleteAnnouncement.php";
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
                                    mAnnouncementList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position,mAnnouncementList.size()-position);//更新适配器这条后面列表的变化
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
        });
    }

    @Override
    public int getItemCount() {
        return mAnnouncementList.size();
    }

}
