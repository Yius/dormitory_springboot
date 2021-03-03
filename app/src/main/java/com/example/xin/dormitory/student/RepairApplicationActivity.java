package com.example.xin.dormitory.student;

import android.content.SharedPreferences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RepairApplicationActivity extends AppCompatActivity {

    private Button repair_apply_commit;
    private EditText et_RepairName;
    private EditText et_DamageCause;
    private EditText et_Details;
    private EditText et_Contact;
    private EditText et_OtherRemarks;
    private Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_application);
        Toolbar toolbar_repair = findViewById(R.id.toolbar);
        toolbar_repair.setTitle("");
        setSupportActionBar(toolbar_repair);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repair_apply_commit = findViewById(R.id.repair_apply_commit);
        et_RepairName = findViewById(R.id.et_RepairName);
        et_DamageCause = findViewById(R.id.et_DamageCause);
        et_Details = findViewById(R.id.et_Details);
        et_Contact = findViewById(R.id.et_Contact);
        et_OtherRemarks = findViewById(R.id.et_OtherRemarks);
        setListeners();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void setListeners() {
        OnClick onClick = new OnClick();
        repair_apply_commit.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch(v.getId()) {
                case R.id.repair_apply_commit:
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String ApplyDate = formatter.format(new Date(System.currentTimeMillis()));
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    String dormID = pref.getString("dormID", "");
                    String RepairName = et_RepairName.getText().toString();
                    String DamageCause = et_DamageCause.getText().toString();
                    String Details = et_Details.getText().toString();
                    String Contact = et_Contact.getText().toString();
                    String OtherRemarks = et_OtherRemarks.getText().toString();
                    String belong = pref.getString("belong", "");
                    myAnimation= AnimationUtils.loadAnimation(RepairApplicationActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    OkHttpClient client = new OkHttpClient();
                    if(RepairName.equals("")||Contact.equals("")){
                        Toast.makeText(MyApplication.getContext(), "维修物件不能为空和联系方式不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        RequestBody requestBody = new FormBody.Builder().add("ApplyDate", ApplyDate).add("dormID", dormID).add("RepairName", RepairName).add("DamageCause", DamageCause)
                                .add("Details", Details).add("Contact", Contact).add("OtherRemarks", OtherRemarks).add("belong",belong).build();
                        //服务器地址，ip地址需要时常更换
                        String address = HttpUtil.address + "repairApply.php";
                        Request request = new Request.Builder().url(address).post(requestBody).build();
                        //匿名内部类实现回调接口
                        client.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                //子线程中操作Toast会出现问题，所以用runOnUiThread
                                if (HttpUtil.parseSimpleJSONData(responseData)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "提交成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "提交失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
