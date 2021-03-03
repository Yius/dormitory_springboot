package com.example.xin.dormitory.houseparent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.BottomNavigationBarUtils;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.utils.Utils;

/**
 * 宿管主界面首页面（oyx新加）
 */
public class IndexHFragment extends Fragment {

    private LinearLayout linearLayout;

    private TextView name;

    private RelativeLayout wx_handle;
    private RelativeLayout announcement;
    private RelativeLayout check_in;
    private RelativeLayout depart_stay;

    public static IndexHFragment newInstance(String title) {
        Bundle args = new Bundle();
        IndexHFragment fragment = new IndexHFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_h,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLinearLayout();
        initTextView();
        initRelativeLayout();
    }


    //初始化主界面，适应不同的手机屏幕大小
    private void initLinearLayout(){
        linearLayout = getView().findViewById(R.id.ll_index_h);
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
        lp.width = 5*lp.height/7;
        linearLayout.setLayoutParams(lp);
    }

    private void initTextView(){
        name = getView().findViewById(R.id.tv_name_h);
        SharedPreferences pref = getContext().getSharedPreferences("dataH", getContext().MODE_PRIVATE);
        name.setText(pref.getString("name","")+"宿管");
    }

    //主界面四个功能的点击事件
    private void initRelativeLayout(){
        OnClick onClick = new OnClick();
        wx_handle = getView().findViewById(R.id.rl_weixiu_handle);
        announcement = getView().findViewById(R.id.rl_announcement);
        check_in = getView().findViewById(R.id.rl_check_in);
        depart_stay = getView().findViewById(R.id.rl_depart_stay);
        wx_handle.setOnClickListener(onClick);
        announcement.setOnClickListener(onClick);
        check_in.setOnClickListener(onClick);
        depart_stay.setOnClickListener(onClick);
    }

    private class OnClick implements RelativeLayout.OnClickListener {
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.rl_weixiu_handle:
                    intent = new Intent(getActivity(), RepairActivity.class);
                    break;
                case R.id.rl_announcement:
                    intent = new Intent(getActivity(), DeliverAnnouncementActivity.class);
                    break;
                case R.id.rl_check_in:
                    intent = new Intent(getActivity(), SignRecordSituationActivity.class);
                    break;
                case R.id.rl_depart_stay:
                    intent = new Intent(getActivity(), StayAndDepartActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
