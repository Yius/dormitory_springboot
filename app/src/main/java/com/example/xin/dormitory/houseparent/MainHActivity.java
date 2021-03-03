package com.example.xin.dormitory.houseparent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.AvatarUtil;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 宿管主界面（oyx新加）
 */

public class MainHActivity extends AppCompatActivity {

    @BindView(R.id.viewpager_h)
    ViewPager viewPager;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.titlebar_h)
    TitleBar titleBar_h;

    private MenuItem menuItem;
    private String[] titles = {"首页","我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.translucent(this);
        setContentView(R.layout.activity_main_h);
        ButterKnife.bind(this);
        saveInfo();
        initTitleBar();
        initBottomNavView();
        initViewPager();

    }

    //保存登录用户的信息
    private void saveInfo() {
        //把信息存到sharedpreferences里
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("ID", HttpUtil.HID).build();
        //服务器地址，ip地址需要时常更换
        String address=HttpUtil.address+"infoH.php";
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
                    SharedPreferences.Editor editor = getSharedPreferences("dataH",MODE_PRIVATE).edit();
                    editor.putString("ID",jsonObject.getString("ID"));
                    editor.putString("name",jsonObject.getString("name"));
                    editor.putString("phone",jsonObject.getString("phone"));
                    editor.putString("govern",jsonObject.getString("govern"));
                    editor.apply();
                    AvatarUtil.loadAvatar(jsonObject.getString("ID"),"houseparent");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTitleBar(){
        titleBar_h.disableLeftView();
    }

    private void initBottomNavView() {
        //BottomNavigationViewHelper.disableShifting(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_index:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_me:
                                viewPager.setCurrentItem(1);
                                break;
                        }
                        return false;
                    }
                }
//                item -> {
//                    switch (item.getItemId()) {
//                        case R.id.item_index:
//                            viewPager.setCurrentItem(0);
//                            break;
//                        case R.id.item_me:
//                            viewPager.setCurrentItem(1);
//                            break;
//                    }
//                    return false;
//                }
        );
    }
    private void initViewPager(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                titleBar_h.setTitle(titles[position]);
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(IndexHFragment.newInstance("首页"));
        adapter.addFragment(MeHFragment.newInstance("我"));
        viewPager.setAdapter(adapter);

    }
}
