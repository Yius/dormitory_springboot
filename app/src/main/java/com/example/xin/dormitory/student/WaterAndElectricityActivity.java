package com.example.xin.dormitory.student;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.example.xin.dormitory.houseparent.StayAndDepartActivity;
import com.xuexiang.xui.widget.guidview.FocusShape;
import com.xuexiang.xui.widget.guidview.GuideCaseQueue;
import com.xuexiang.xui.widget.guidview.GuideCaseView;
import com.xuexiang.xui.widget.guidview.OnViewInflateListener;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 水电费界面（oyx新加）
 */

public class WaterAndElectricityActivity extends AppCompatActivity {

    @BindView(R.id.super_tv_water)
    SuperTextView tv_water;

    @BindView(R.id.expandable_layout_water)
    ExpandableLayout expandableLayout1;

    @BindView(R.id.super_tv_electricity)
    SuperTextView tv_electricity;

    @BindView(R.id.expandable_layout_electricity)
    ExpandableLayout expandableLayout2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_web_site_water)
    TextView web_site_water;

    @BindView(R.id.tv_web_site_electricity)
    TextView web_site_electricity;

    @BindView(R.id.super_btn_water)
    SuperButton water;

    @BindView(R.id.super_btn_electricity)
    SuperButton electricity;

    @BindView(R.id.tv_dormID)
    TextView dormID;

    private ActionBar actionBar;
    String waterUrl = null;
    String electricityUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtils.translucent(this);
        setContentView(R.layout.activity_water_and_electricity);
        ButterKnife.bind(this);
        initToolbar();
        initUrls();
        initDormID();
        initWater();
        initElectricity();
        showTextGuideView();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initUrls(){
        String dorm = getIntent().getStringExtra("dorm");
        if(dorm!=null&&!dorm.equals("")){
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("dorm",dorm).build();
            //服务器地址，ip地址需要时常更换
            String address=HttpUtil.address+"getWaterAndElectricity.php";
            Request request = new Request.Builder().url(address).post(requestBody).build();
            //匿名内部类实现回调接口
            client.newCall(request).enqueue(new okhttp3.Callback(){

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法获取水电费网址",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        waterUrl = jsonObject.getString("waterUrl");
                        electricityUrl = jsonObject.getString("electricityUrl");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initTextView();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyApplication.getContext(),"你所属宿舍楼为空，无法获取水电费网址",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initDormID(){
        dormID.setText(getIntent().getStringExtra("dormID"));
    }

    private void initTextView(){
        if(waterUrl!=null&&!waterUrl.equals("")){
            web_site_water.setText(waterUrl);
            water.setVisibility(View.VISIBLE);
            water.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WaterAndElectricityActivity.this,CheckEleAndWaterActivity.class);
                    intent.putExtra("url",waterUrl);
                    startActivity(intent);
                }
            });
        }
        if(electricityUrl!=null&&!electricityUrl.equals("")){
            web_site_electricity.setText(electricityUrl);
            electricity.setVisibility(View.VISIBLE);
            electricity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WaterAndElectricityActivity.this,CheckEleAndWaterActivity.class);
                    intent.putExtra("url",electricityUrl);
                    startActivity(intent);
                }
            });
        }
    }

    private void initWater(){
        expandableLayout1.setOnExpansionChangedListener(new ExpandableLayout.OnExpansionChangedListener() {
            @Override
            public void onExpansionChanged(float expansion, int state) {
                if (tv_water != null && tv_water.getRightIconIV() != null) {
                    tv_water.getRightIconIV().setRotation(expansion * 90);
                    tv_water.useShape();
                    if(state<=1){
                        tv_water.setShapeCornersBottomLeftRadius(15);
                        tv_water.setShapeCornersBottomRightRadius(15);
                    }else{
                        tv_water.setShapeCornersBottomLeftRadius(0);
                        tv_water.setShapeCornersBottomRightRadius(0);
                    }
                }
            }
        });
        tv_water.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                if (expandableLayout1!= null) {
                    expandableLayout1.toggle();
                }
            }
        });
    }


    private void initElectricity(){
        expandableLayout2.setOnExpansionChangedListener(new ExpandableLayout.OnExpansionChangedListener() {
            @Override
            public void onExpansionChanged(float expansion, int state) {
                if (tv_electricity != null && tv_electricity.getRightIconIV() != null) {
                    tv_electricity.getRightIconIV().setRotation(expansion * 90);
                    tv_electricity.useShape();
                    if(state<=1){
                        tv_electricity.setShapeCornersBottomLeftRadius(15);
                        tv_electricity.setShapeCornersBottomRightRadius(15);
                    }else{
                        tv_electricity.setShapeCornersBottomLeftRadius(0);
                        tv_electricity.setShapeCornersBottomRightRadius(0);
                    }
                }
            }
        });
        tv_electricity.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                if (expandableLayout2!= null) {
                    expandableLayout2.toggle();
                }
            }
        });
    }

    private GuideCaseView guideStep2;

    private void showTextGuideView() {

        final GuideCaseView guideStep1 = new GuideCaseView.Builder(WaterAndElectricityActivity.this)
                .title("点击可访问水费查询网址")
                .focusOn(tv_water)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .build();

        guideStep2 = new GuideCaseView.Builder(WaterAndElectricityActivity.this)
                .focusOn(tv_electricity)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .customView(R.layout.glide_for_water_and_electricity, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        view.findViewById(R.id.btn_action_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //                                字符串应保证每个不同的activity都不同
                                GuideCaseView.setShowOnce(WaterAndElectricityActivity.this,"4");
                                guideStep2.hide();
                            }
                        });
                    }
                })
                .build();

        if(!GuideCaseView.isShowOnce(WaterAndElectricityActivity.this,"4")){
            new GuideCaseQueue()
                    .add(guideStep1)
                    .add(guideStep2)
                    .show();
        }

    }
}

