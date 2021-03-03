package com.example.xin.dormitory.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.Utility.AvatarUtil;
import com.example.xin.dormitory.common.TabView;
import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生端界面（oyx新加）
 */
public class MainSActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindArray(R.array.tab_array)
    String[] mTabTitles;

    @BindView(R.id.tab_index)
    TabView mTabIndex;

    @BindView(R.id.tab_posts)
    TabView mTabPosts;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.exit)
    NavigationView exit;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fl_bottomBar)
    FrameLayout BottomBar;


    private AppBarLayout.LayoutParams mParams;

    private List<TabView> mTabViews  = new ArrayList<>();
    private static final int INDEX = 0;
    private static final int POSTS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //下面这句话必须在 setContentView()方法前，否则闪退
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtils.translucent(this);
        setContentView(R.layout.activity_main_s);

        ButterKnife.bind(this);

        StatusBarUtils.getStatusBarHeight(this);
        //启动服务
//        startService(new Intent(this,ForegroundService.class));

        saveInfo();

        initTitleBar();
        initFab();
        initBottomBar();
        initDrawerLayout();
//        initData();
//        initPosts();

    }

    //保存登录用户的信息及初始化水电费地址
    private void saveInfo(){
//        //初始化HttpUtil中的水电费地址
//        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
//        HttpUtil.waterCheck = pref.getString("waterCheck",null);
//        HttpUtil.electricityCheck = pref.getString("electricityCheck",null);
        //把信息存到sharedpreferences里
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("ID",HttpUtil.ID).build();
        //服务器地址，ip地址需要时常更换
        String address=HttpUtil.address+"infoS.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法获取您的信息",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    //存储除密码以外的所有信息
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("ID",jsonObject.getString("ID"));
                    editor.putString("name",jsonObject.getString("name"));
                    editor.putString("dormID",jsonObject.getString("dormID"));
                    editor.putString("phone",jsonObject.getString("phone"));
                    editor.putString("nickname",jsonObject.getString("nickname"));
                    editor.putString("belong",jsonObject.getString("belong"));
                    editor.apply();

                    //登录后获取头像并保存到本地
                    AvatarUtil.loadAvatar(jsonObject.getString("ID"),"student");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initHead();
                    }
                });

            }
        });
    }

    private void initTitleBar(){
        toolbar.setTitle(mTabTitles[0]);
        toolbar.setHorizontalScrollBarEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
    }
    private void initFab(){
        fab.hide();
    }
    private void initBottomBar(){
        mTabViews.add(mTabIndex);
        mTabViews.add(mTabPosts);
        mViewPager.setOffscreenPageLimit(mTabTitles.length - 1);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            /**
             * @param position 滑动的时候，position总是代表左边的View， position+1总是代表右边的View
             * @param positionOffset 左边View位移的比例
             * @param positionOffsetPixels 左边View位移的像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 左边View进行动画
                mTabViews.get(position).setXPercentage(1 - positionOffset);
                // 如果positionOffset非0，那么就代表右边的View可见，也就说明需要对右边的View进行动画
                if (positionOffset > 0) {
                    mTabViews.get(position + 1).setXPercentage(positionOffset);
                }
                if(position==0&&positionOffset>=0.5) {
                    toolbar.setTitle(mTabTitles[1]);
                    fab.show();
                    mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                }else if(position==0&&positionOffset<0.5){
                    toolbar.setTitle(mTabTitles[0]);
                    fab.hide();
                    mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
                }else{
                    fab.show();
                    mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                }
                if(BottomBar.getTranslationY()>0)
                    BottomBar.setTranslationY(0);
            }
        });
    }
    //侧滑
    private void initDrawerLayout(){
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                drawerLayout.closeDrawer(navView);
                switch(item.getItemId()){
                    case R.id.nav_info:
                        intent = new Intent(MainSActivity.this,PersonalInfoActivity.class);
                        break;
                    case R.id.nav_sign_in:
                        intent = new Intent(MainSActivity.this,CheckSignNoticesActivity.class);
                        break;
                    case R.id.nav_contact_us:
                        intent = new Intent(MainSActivity.this, AboutUs.class);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
                item.setCheckable(false);
                return true;
            }
        });
        exit.setItemIconTintList(null);
        exit.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_exit:
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    private void initHead(){
        View headerLayout = navView.getHeaderView(0);
        TextView headName = headerLayout.findViewById(R.id.tv_head_name);
        TextView headID = headerLayout.findViewById(R.id.tv_head_id);
        TextView headDormID = headerLayout.findViewById(R.id.tv_head_dormID);
        RadiusImageView headAvatar = headerLayout.findViewById(R.id.head_avatar);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        headID.setText(pref.getString("ID",""));
        headDormID.setText(pref.getString("dormID",""));
        headName.setText(pref.getString("name",""));
        //侧滑头像设置
        AvatarUtil.loadAvatar(this,headAvatar,headID.getText().toString(),"student");
    }

    private void updateCurrentTab(int index) {
        toolbar.setTitle(mTabTitles[index]);
        for (int i = 0; i < mTabViews.size(); i++) {
            if (index == i) {
                mTabViews.get(i).setXPercentage(1);
            } else {
                mTabViews.get(i).setXPercentage(0);
            }
        }
    }
    @OnClick({R.id.tab_index, R.id.tab_posts})
    public void onClickTab(View v) {
        switch (v.getId()) {
            case R.id.tab_index:
                mViewPager.setCurrentItem(INDEX, false);
                updateCurrentTab(INDEX);
                break;
            case R.id.tab_posts:
                mViewPager.setCurrentItem(POSTS, false);
                updateCurrentTab(POSTS);
                break;
        }

    }
    @OnClick({R.id.rl_add_post})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.rl_add_post:
                intent = new Intent(MainSActivity.this,AddPostsActivity.class);
                break;
        }
        startActivity(intent);
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return getTabFragment(i, mTabTitles[i]);
        }
        @Override
        public int getCount() {
            return mTabTitles.length;
        }
    }
    private Fragment getTabFragment(int index, String title) {
        Fragment fragment = null;
        switch (index) {
            case INDEX:
                fragment = IndexSFragment.newInstance(title);
                break;
            case POSTS:
                fragment = PostsSFragment.newInstance(title);
                break;
        }
        return fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initHead();
            }
        });
    }
}
