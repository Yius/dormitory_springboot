package com.example.xin.dormitory.houseparent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 此类将被HandledRepairActivity和UnhandledRepairActivity共用
 */
public class RepairDetailsActivity extends AppCompatActivity {

    private TextView tv_dormIDDetail;
    private TextView tv_RepairName;
    private TextView tv_DamageCause;
    private TextView tv_Details;
    private TextView tv_Contact;
    private TextView tv_OtherRemarks;
    private Button bt_handled;
    private Repair repair;
    private Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_details);
        initLayout();
    }

    /**
     * 把布局初始化的代码写在一起
     */
    private void initLayout(){
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        tv_Contact = findViewById(R.id.tv_Contact);
        tv_DamageCause = findViewById(R.id.tv_DamageCause);
        tv_Details = findViewById(R.id.tv_Details);
        tv_Details.setMovementMethod(new ScrollingMovementMethod());
        tv_dormIDDetail = findViewById(R.id.tv_dormIDDetail);
        tv_OtherRemarks = findViewById(R.id.tv_OtherRemarks);
        tv_OtherRemarks.setMovementMethod(new ScrollingMovementMethod());
        tv_RepairName = findViewById(R.id.tv_RepairName);
        bt_handled = findViewById(R.id.bt_handled);
        repair = (Repair)getIntent().getSerializableExtra("repair_data");
        tv_Contact.setText(repair.getContact());
        tv_DamageCause.setText(repair.getDamageCause());
        tv_Details.setText(repair.getDetails());
        tv_dormIDDetail.setText(repair.getDormID());
        tv_OtherRemarks.setText(repair.getOtherRemarks());
        tv_RepairName.setText(repair.getRepairName());
        //根据status设置button可见性，0代表未处理，1代表已处理
        int repairStatus = repair.getStatus();
        if(repairStatus==0){
            bt_handled.setVisibility(View.VISIBLE);
        }
        setListeners();
    }


    /**
     * 监听器初始化
     */
    private void setListeners(){
        OnClick onClick = new OnClick();
        bt_handled.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            switch(v.getId()) {
                case R.id.bt_handled:
                    myAnimation= AnimationUtils.loadAnimation(RepairDetailsActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("ApplyID",repair.getApplyID()+"").build();
                    //服务器地址，ip地址需要时常更换
                    String address=HttpUtil.address+"alterRepairInfo.php";
                    Request request = new Request.Builder().url(address).post(requestBody).build();
                    //匿名内部类实现回调接口
                    client.newCall(request).enqueue(new okhttp3.Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法修改",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            if(HttpUtil.parseSimpleJSONData(responseData)){
                                //子线程中操作Toast会出现问题，所以用runOnUiThread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(),"修改失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                    break;
            }
        }
    }

}
