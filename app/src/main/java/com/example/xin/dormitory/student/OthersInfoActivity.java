package com.example.xin.dormitory.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.AvatarUtil;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 他人信息界面（oyx新加）
 */
public class OthersInfoActivity extends AppCompatActivity {

    @BindView(R.id.appBar_personal_info)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar_personal_info)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.personal_img)
    RadiusImageView personalImg;

    @BindView(R.id.rl_personal_info)
    RelativeLayout relativeLayout;

    @BindView(R.id.rv_personal_posts)
    RecyclerView recyclerView;

    @BindView(R.id.personal_name)
    TextView personalName;

    @BindView(R.id.tv_nickname)
    TextView nickname;

    @BindView(R.id.tv_id)
    TextView id;

    @BindView(R.id.tv_dormBelong)
    TextView dormBelong;

    @BindView(R.id.tv_phone)
    TextView phone;

    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<Post> mPostList = new ArrayList<>();
    private PersonalPostAdapter adapter;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 19) {
//            //After LOLLIPOP not translucent status bar
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        setContentView(R.layout.activity_others_info);
        StatusBarUtils.translucent(this);
        ButterKnife.bind(this);
        initRefreshLayout();
        initInfo();
        initListener();
        initToolbar();
        initCollapsingToolbar();
        //initImageView();
        initPosts();
        initRecyclerView();
    }
    private void initRefreshLayout(){
        //mMaterialHeader = (MaterialHeader)smartRefreshLayout.getRefreshHeader();
        smartRefreshLayout.setEnableHeaderTranslationContent(true);
        ClassicsHeader header = new ClassicsHeader(this);
        header.setSpinnerStyle(SpinnerStyle.Translate);
        setThemeColor(R.color.titleBar_color);
        smartRefreshLayout.setRefreshHeader(header);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshPosts();
            }
        });
        smartRefreshLayout.autoRefresh();
    }

    private void setThemeColor(int colorPrimary) {
        smartRefreshLayout.setPrimaryColorsId(colorPrimary, android.R.color.white);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimary));
//        }
    }
    private void initInfo(){
        nickname.setText(getIntent().getStringExtra("contactNickName"));
        phone.setText(getIntent().getStringExtra("contactPhone"));
        id.setText(getIntent().getStringExtra("contactID"));
        dormBelong.setText(getIntent().getStringExtra("contactBelong"));
        personalName.setText(getIntent().getStringExtra("contactName"));
        //获取头像
        AvatarUtil.setAvatar(this,personalImg,id.getText().toString(),"student");
    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = getWindow();
//            //After LOLLIPOP not translucent status bar
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //Then call setStatusBarColor.
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.titlerBar_color));
//        }
    }

    private void initCollapsingToolbar(){
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    private void initListener(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                relativeLayout.setAlpha(1-(float)(-verticalOffset)/490);
                if (actionBar.getHeight()+StatusBarUtils.getStatusBarHeight(OthersInfoActivity.this)- collapsingToolbarLayout.getHeight() == verticalOffset) {
                    collapsingToolbarLayout.setTitle(personalName.getText());
                }else{
                    collapsingToolbarLayout.setTitle("");
//                    actionBar.setDisplayHomeAsUpEnabled(false);
                }

            }
        });
    }
    //    private void initImageView(){
//        int ImageId = 0;
//        Glide.with(this).load(ImageId).into(imageView);
//    }
    private void initPosts(){
        mPostList.clear();
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("ID",id.getText().toString()).build();
            Request request = new Request.Builder().url(HttpUtil.address + "getPersonalPosts.php").post(requestBody).build();
//            String responseData = response.body().string();
            client.newCall(request).enqueue(new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法获取信息",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Post post = new Post();
                            post.setPosterName(jsonObject.getString("name"));
                            post.setPosterID(jsonObject.getString("ID"));
//                            String PostsDate = jsonObject.getString("PostsDate").substring(0,10);
//                            String PostsTime = jsonObject.getString("PostsDate").substring(11,19);
                            post.setPostingDate(jsonObject.getString("PostsDate"));
                            post.setPostTitle(jsonObject.getString("postsTitle"));
                            post.setPostContent(jsonObject.getString("postsContent"));
                            post.setPostID(Integer.parseInt(jsonObject.getString("PostsID")));
                            post.setLatestReplyTime(jsonObject.getString("LatestReplyTime"));
                            mPostList.add(0, post);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(MyApplication.getContext(),"数据加载完成", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
//        Post post = new Post(1,R.mipmap.ic_launcher,"張三","1001","2020-03-13 13:11:11","深度解析RocketMQ消息发送的高可用设计","从rocketmq topic的创建机制可知，一个topic对应有多个消息队列，那么我们在发送消息时，是如何选择消息队列进行发送的？","2020-03-16");
//        for(int i=0;i<10;i++){
//            mPostList.add(post);
//        }
    }
    private void refreshPosts(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initInfo();
                        initPosts();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                });
            }
        }).start();
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonalPostAdapter(mPostList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPosts();
    }
}
