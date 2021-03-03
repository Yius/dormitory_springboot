package com.example.xin.dormitory.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.BottomNavigationBarUtils;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.example.xin.dormitory.common.Announcement;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 主界面首页面（oyx新加）
 */
public class IndexSFragment extends Fragment {

    private LinearLayout linearLayout;
    private RelativeLayout rl_wexiu;
    private RelativeLayout rl_shuidian;
    private RelativeLayout rl_lisu;
    private RelativeLayout rl_liusu;
    private TextView moreAnnouncement;

    public static IndexSFragment newInstance(String title) {
        Bundle args = new Bundle();
        IndexSFragment fragment = new IndexSFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_s,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLinearLayout();
        initRelativeLayouts();
        initMoreAnnouncement();
        initAnnouncementsTitle();
    }

    //初始化主界面，适应不同的手机屏幕大小
    private void initLinearLayout(){
        linearLayout = getView().findViewById(R.id.ll_index);
        ViewGroup.LayoutParams lp;
        lp = linearLayout.getLayoutParams();
        //int w =DensityUtils.px2dp(Utils.getScreenWidth(view.getContext()));

        //获取当前底部虚拟按键的高度（不存在为0）
        //int cur_bh = BottomNavigationBarUtils.getNavigationBarHeightIfRoom(getContext());
        //获取非全面屏下虚拟按键的高度（无论是否隐藏）
        //int real_bh= BottomNavigationBarUtils.getNavigationBarHeight(getContext());

        //屏幕当前的高度（有虚拟按键时除去的高度）（单位是px）
        int h = Utils.getScreenHeight(getContext());

        //底部导航栏高度（55dp）(单位为px)
        int bottom_bar_h = DensityUtils.dp2px(55);

        //状态栏的高度
        int status_bar_h = StatusBarUtils.getStatusBarHeight(getContext());

        //toolbar的高度 采用?attr/actionBarSize 默认高度为56dp
        int toolbar_h = DensityUtils.dp2px(56);

        //四个按钮与下方导航栏的距离 15dp
        int margin_h = DensityUtils.dp2px(15);

        lp.height = h-bottom_bar_h-status_bar_h-toolbar_h-margin_h;
        lp.width = 2*lp.height/3;
        linearLayout.setLayoutParams(lp);
    }

    //设置四个功能的点击事件
    private void initRelativeLayouts(){
        OnClick onClick = new OnClick();
        rl_wexiu = getView().findViewById(R.id.rl_weixiu);
        rl_shuidian = getView().findViewById(R.id.rl_shuidian);
        rl_lisu = getView().findViewById(R.id.rl_lisu);
        rl_liusu = getView().findViewById(R.id.rl_liusu);
        rl_wexiu.setOnClickListener(onClick);
        rl_shuidian.setOnClickListener(onClick);
        rl_lisu.setOnClickListener(onClick);
        rl_liusu.setOnClickListener(onClick);
    }

    private void initAnnouncementsTitle(){
        final TextView textViews[] = {getView().findViewById(R.id.tv_announcement_title_1),
                getView().findViewById(R.id.tv_announcement_title_2),
                getView().findViewById(R.id.tv_announcement_title_3)};

        SharedPreferences pref = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("belong",pref.getString("belong","")).build();
        //服务器地址，ip地址需要时常更换
        String address= HttpUtil.address+"checkThreeAnnouncements.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法获取信息", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(responseData);
                            Log.d("length",""+jsonArray.length());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                final Announcement announcement = new Announcement(jsonObject);
                                textViews[i].setText(jsonObject.getString("title"));
                                textViews[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(),CheckAnnouncementDetailsActivity.class);
                                        intent.putExtra("announcement_data",announcement);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyApplication.getContext(),"数据加载完成",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initMoreAnnouncement(){
        moreAnnouncement = getView().findViewById(R.id.tv_more_announcement);
        moreAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CheckAnnouncementNoticesActivity.class);
                startActivity(intent);
            }
        });
    }

    private class OnClick implements RelativeLayout.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch(v.getId()) {
                case R.id.rl_weixiu:
                    intent = new Intent(getActivity(), RepairApplicationActivity.class);
                    break;
                case R.id.rl_shuidian:
                    //水电费水电费
                    intent = new Intent(getActivity(), WaterAndElectricityActivity.class);
                    intent.putExtra("dormID",getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).getString("dormID",""));
                    intent.putExtra("dorm",getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).getString("belong",""));
                    break;
                case R.id.rl_lisu:
                    intent = new Intent(getActivity(),DepartRegisterActivity.class);
                    break;
                case R.id.rl_liusu:
                    intent = new Intent(getActivity(), StayRegisterActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}

