package com.example.xin.dormitory.houseparent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.example.xin.dormitory.common.Announcement;
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

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckHistoryAnnouncementActivity extends AppCompatActivity {

    private SmartRefreshLayout smartRefresh;
    private RecyclerView recycleView;
    private List<Announcement> announcementList = new ArrayList<>();
    private AnnouncementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_history_announcement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        smartRefresh = findViewById(R.id.smart_refresh);
        smartRefresh.setRefreshHeader(getRefreshHeader("BezierCircleHeader"));
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshAnnoucements();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView = findViewById(R.id.recycle_view);
        recycleView.setLayoutManager(layoutManager);
        adapter = new AnnouncementAdapter(announcementList);
        recycleView.setAdapter(adapter);

        initAnnouncements();
    }

    /**
     * 初始化显示历史通知
     */
    private void initAnnouncements(){
        announcementList.clear();

        //学生可以接受属于这栋楼的公告，而宿管只能看自己发布的公告
        SharedPreferences pref = getSharedPreferences("dataH",MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("houseparentID",pref.getString("ID","")).build();
        //服务器地址，ip地址需要时常更换
        String address=HttpUtil.address+"checkAnnouncementInfo.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
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
                    for(int i=0;i<jsonArray.length();++i){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        announcementList.add(new Announcement(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //通知数据改变，涉及UI变化，故在runOnUiThread中操作
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MyApplication.getContext(),"数据加载完成",Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }


    public void refreshAnnoucements(){
        //网络操作耗时，故开子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAnnouncements();
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
