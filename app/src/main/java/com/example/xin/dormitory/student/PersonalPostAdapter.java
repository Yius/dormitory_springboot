package com.example.xin.dormitory.student;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.AvatarUtil;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 个人帖子适配器（oyx新加）
 */
public class PersonalPostAdapter extends RecyclerView.Adapter<PersonalPostAdapter.ViewHolder>{
    private Context mContext;

    private List<Post> mPostList;

    private Activity mActivity;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView posterImage;
        TextView posterName;
        TextView postingDate;
        // TextView postingTime;
        TextView postTitle;
        TextView postContent;
        ImageView deletePost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            posterImage = itemView.findViewById(R.id.iv_personal_avatar);
            posterName = itemView.findViewById(R.id.tv_personal_name);
            postingDate = itemView.findViewById(R.id.tv_personal_postdate);
            postTitle = itemView.findViewById(R.id.tv_personal_title);
            postContent = itemView.findViewById(R.id.tv_personal_content);
            deletePost = itemView.findViewById(R.id.delete_post);
        }
    }

    public PersonalPostAdapter(List<Post> postList, Activity activity){
        mPostList = postList;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.personal_post_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Post post = mPostList.get(position);
                Intent intent = new Intent(v.getContext(), PostsChatActivity.class);
                intent.putExtra("PostsID", ""+ post.getPostID());
                intent.putExtra("Title", post.getPostTitle());
                intent.putExtra("Content", post.getPostContent());
                mContext.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Post post = mPostList.get(position);
        holder.posterName.setText(post.getPosterName());
        holder.postTitle.setText(post.getPostTitle());
        holder.postContent.setText(post.getPostContent());
        SharedPreferences pref = mActivity.getSharedPreferences("data",mActivity.MODE_PRIVATE);
        if(!(post.getPosterID()).equals(pref.getString("ID",""))){
            holder.deletePost.setVisibility(ImageView.INVISIBLE);
        }else {
            holder.deletePost.setVisibility(ImageView.VISIBLE);
            holder.deletePost.setOnClickListener(new View.OnClickListener() {
                String PostsID = "" + post.getPostID();

                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
                            .setTitle("删除该帖子").setMessage("是否删除？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPostList.remove(position);
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });
                                    try {
                                        OkHttpClient client = new OkHttpClient();
                                        RequestBody requestBody = new FormBody.Builder().add("PostsID", PostsID).build();
                                        //服务器地址，ip地址需要时常更换
                                        String address = HttpUtil.address + "deletePosts.php";
                                        Request request = new Request.Builder().url(address).post(requestBody).build();
                                        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                e.printStackTrace();

                                                mActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(MyApplication.getContext(), "服务器连接失败，无法删除", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                mActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(MyApplication.getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).create();
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_corner);
                }
            });
        }
        //获取最近一次回复时间并统一显示格式
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = formatter.format(new Date(System.currentTimeMillis()));
            Date d1 = formatter.parse(currentTime);
            Date d2 = formatter.parse(post.getPostingDate());
            String str;
            long time = (d1.getTime() - d2.getTime())/1000;
            if(time<60){
                str = time+"秒前";
            }else if(time/60 < 60){
                str = (time/60+1) + "分钟前";
            }else if(time/3600 < 24){
                str = (time/3600) +"小时前";
            }else if(time/86400 < 4){
                str = (time/86400) + "天前";
            }else{
                str = post.getLatestReplyTime();
            }
            holder.postingDate.setText(str);
        }catch(ParseException e){
            e.printStackTrace();
        }

        AvatarUtil.setAvatar(MyApplication.getContext(),holder.posterImage,post.getPosterID(),"student");
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}
