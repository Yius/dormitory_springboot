package com.example.xin.dormitory.student;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 主界面帖子切换页（oyx新加）
 */
public class PostsSFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;

    private List<Post> mPostList = new ArrayList<>();
    private PostAdapter adapter;

    public static PostsSFragment newInstance(String title) {
        Bundle args = new Bundle();
        PostsSFragment fragment = new PostsSFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_s,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSmartRefresh();
        initFab();
        initPosts();
        initRecyclerView();
    }

    private void initPosts(){
        mPostList.clear();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(HttpUtil.address + "getPosts.php").build();
//            String responseData = response.body().string();
            client.newCall(request).enqueue(new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
//                    getActivity().runOnUiThread(new Runnable() {
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

    private void initRecyclerView(){
        recyclerView = getView().findViewById(R.id.recycler_view_posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostAdapter(mPostList);
        recyclerView.setAdapter(adapter);
    }
    private void initFab(){
        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
                smartRefresh.autoRefresh();
                refreshPosts();
            }
        });
    }

    private void initSmartRefresh(){
        smartRefresh = getView().findViewById(R.id.smart_refresh);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshPosts();
//            }
//        });
        smartRefresh.setRefreshHeader(getRefreshHeader("FunGameHitBlockHeader"));
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshPosts();
            }
        });
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPosts();
                        smartRefresh.finishRefresh();
                    }
                });
            }
        }).start();
    }


    private RefreshHeader getRefreshHeader(String name) {
        try {
            Class<?> headerClass = Class.forName("com.scwang.smartrefresh.header." + name);
            Constructor<?> constructor = headerClass.getConstructor(Context.class);
            return  (RefreshHeader) constructor.newInstance(MyApplication.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

