package com.example.xin.dormitory.houseparent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.xuexiang.xui.widget.button.ButtonView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetWaterAndElectricityActivity extends AppCompatActivity {

    EditText et_water;
    EditText et_electricity;
    ButtonView bt_ok;

    private String dorm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_water_and_electricity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        dorm = pref.getString("govern","");
        initViews();
        initEditTextData();
        setListener();
    }

    private void initViews() {
        et_water = findViewById(R.id.et_water);
        et_electricity = findViewById(R.id.et_electricity);
        bt_ok = findViewById(R.id.bt_ok);
    }

    private void initEditTextData(){
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
                        et_water.setText(jsonObject.getString("waterUrl"));
                        et_electricity.setText(jsonObject.getString("electricityUrl"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyApplication.getContext(),"你所管理宿舍楼号为空，无法获取水电费网址",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void setListener() {
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.bt_ok){
                    //TODO 设置的逻辑
//                    finish();
                    String waterUrl = et_water.getText().toString();
                    String electricityUrl = et_electricity.getText().toString();
                    if(dorm==null||dorm.equals("")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyApplication.getContext(), "您所管理的宿舍楼号为空，无法设置水电费网址", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {

                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder().add("dorm", dorm).add("waterUrl", waterUrl).add("electricityUrl", electricityUrl).build();
                        //服务器地址，ip地址需要时常更换
                        String address = HttpUtil.address + "setWaterAndElectricity.php";
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
                                            Toast.makeText(MyApplication.getContext(), "设置成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "设置失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
