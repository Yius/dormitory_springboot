package com.example.xin.dormitory.student;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private Context mContext;

    private List<Post> mPostList;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView posterImage;
        TextView posterName;
        TextView latestReplyTime;
        TextView postingDate;
        TextView postingTime;
        TextView postTitle;
        TextView postContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            posterImage = itemView.findViewById(R.id.iv_poster_avatar);
            posterName = itemView.findViewById(R.id.tv_poster_name);
            latestReplyTime = itemView.findViewById(R.id.tv_latest_reply_time);
            postingDate = itemView.findViewById(R.id.tv_posting_date);
            postingTime = itemView.findViewById(R.id.tv_posting_time);
            postTitle = itemView.findViewById(R.id.tv_post_title);
            postContent = itemView.findViewById(R.id.tv_post_content);
        }
    }

    public PostAdapter(List<Post> postList){
        mPostList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mPostList.get(position);
        holder.posterName.setText(post.getPosterName());
        String PostingDate = post.getPostingDate().substring(0,10);
        String PostingTime = post.getPostingDate().substring(11,19);
        holder.postingDate.setText(PostingDate);
        holder.postingTime.setText(PostingTime);
        holder.postTitle.setText(post.getPostTitle());
        holder.postContent.setText(post.getPostContent());

        //获取最近一次回复时间并统一显示格式
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = formatter.format(new Date(System.currentTimeMillis()));
            Date d1 = formatter.parse(currentTime);
            Date d2 = formatter.parse(post.getLatestReplyTime());
            String str;
            long time = (d1.getTime() - d2.getTime())/1000;
            if(time<60){
                str = "回复于"+time+"秒前";
            }else if(time/60 < 60){
                str = "回复于"+ (time/60+1) + "分钟前";
            }else if(time/3600 < 24){
                str = "回复于"+(time/3600) +"小时前";
            }else if(time/86400 < 4){
                str = "回复于"+ (time/86400) + "天前";
            }else{
                str = "回复于"+ post.getLatestReplyTime();
            }
            holder.latestReplyTime.setText(str);
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

