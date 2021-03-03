package com.example.xin.dormitory.student;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.example.xin.dormitory.common.Sign;
import com.example.xin.dormitory.houseparent.SignRecordSituationActivity;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//lyf新加，签到详情
public class SignDetails extends AppCompatActivity {

    private ShadowButton sb_sign;
    private TextView tv_title;
    private TextView tv_houseparentName;
    private TextView tv_address;
    private TextView tv_Rtime;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocation aMapLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sb_sign = findViewById(R.id.sign);
        tv_title = findViewById(R.id.tv_title);
        tv_houseparentName = findViewById(R.id.tv_houseparentName);
        tv_address = findViewById(R.id.tv_address);
        tv_Rtime = findViewById(R.id.tv_Rtime);

        firstCheck();

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() != 0) {
                        Toast.makeText(SignDetails.this, "很抱歉，无法获取您的定位信息！", Toast.LENGTH_SHORT).show();
                    } else {
                        aMapLocation = amapLocation;
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
        }
        mLocationClient.stopLocation();
        mLocationClient.startLocation();


        Sign sign = (Sign) getIntent().getSerializableExtra("sign");
        tv_title.setText(sign.getTitle());
        tv_houseparentName.setText(sign.getHouseparentName());
        tv_Rtime.setText(sign.getRtime());
        tv_address.setText(sign.getDetailAddress());
        sb_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SignDetails.this).setTitle("你要现在签到吗？")
                        .setIcon(R.drawable.ic_sign_notice)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (aMapLocation != null || aMapLocation.getErrorCode() == 0) {
                                    //按下确定键后的事件
                                    studentSignHelp(sign, aMapLocation.getLatitude(), aMapLocation.getLongitude());
                                } else {
                                    Toast.makeText(MyApplication.getContext(), "无法定位，无法签到。", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }


    /**
     * @param sign      对应的签到对象
     * @param latitude  当前位置的纬度
     * @param longitude 当前位置的精度
     */
    private void studentSignHelp(Sign sign, double latitude, double longitude) {
        float distance = AMapUtils.calculateLineDistance(new LatLng(sign.getLatitude(), sign.getLongitude()), new LatLng(latitude, longitude));
        if (distance > 100) {
            Toast.makeText(SignDetails.this, "当前距离打卡点的距离超过100米，请尝试到打卡点附近打卡，目前距离为:" + distance + "米", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences pref = MyApplication.getContext().getSharedPreferences("data", MODE_PRIVATE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String signedtime = formatter.format(new Date(System.currentTimeMillis()));
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("signedtime", signedtime).add("SID", pref.getString("ID", "")).add("recordID", String.valueOf(sign.getID())).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "signTimeRecord.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "服务器连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                //子线程中操作Toast会出现问题，所以用runOnUiThread
                if (HttpUtil.parseSimpleJSONData(responseData)) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "签到成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "签到失败,请不要重复签到哦", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }




    //1、首先声明一个数组permissions，将需要的权限都放在里面
    String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE
    };
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码


    public void firstCheck(){
        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        for(int result:grantResults){
            if(result!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(SignDetails.this,"缺少必要的权限！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

}
