package com.example.xin.dormitory.houseparent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.guidview.GuideCaseView;
import com.xuexiang.xui.widget.guidview.OnViewInflateListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeliverAnnouncementActivity extends AppCompatActivity{

    private EditText inner_title;
    private EditText et_content;
    private ButtonView bt_deliver;
    private TextView tv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_announcement);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inner_title = findViewById(R.id.inner_title);
        et_content = findViewById(R.id.et_content);
        bt_deliver = findViewById(R.id.bt_deliver);
        tv_history = findViewById(R.id.tv_history);


        setListeners();
        showTextGuideView();
    }


    private void setListeners(){
        OnClick onClick = new OnClick();
        bt_deliver.setOnClickListener(onClick);
        tv_history.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            switch(v.getId()) {
                case R.id.bt_deliver:
                    String content = et_content.getText().toString();
                    String title = inner_title.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm::ss");
                    String Atime = formatter.format(new Date(System.currentTimeMillis()));
                    SharedPreferences pref = getSharedPreferences("dataH",MODE_PRIVATE);
                    OkHttpClient client = new OkHttpClient();
                    if(content.equals("")||title.equals("")){
                        Toast.makeText(MyApplication.getContext(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        RequestBody requestBody = new FormBody.Builder().add("houseparentID", pref.getString("ID", "")).add("govern",pref.getString("govern","")).add("Atime", Atime).add("title", title).add("content", content).add("houseparentName",pref.getString("name","")).build();
                        //服务器地址，ip地址需要时常更换
                        String address = HttpUtil.address + "createAnnouncement.php";
                        Request request = new Request.Builder().url(address).post(requestBody).build();
                        //匿名内部类实现回调接口
                        client.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "发布失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    break;
                case R.id.tv_history:
                    Intent intent = new Intent(DeliverAnnouncementActivity.this,CheckHistoryAnnouncementActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }


    private GuideCaseView guideCaseView;

    private void showTextGuideView() {

        guideCaseView = new GuideCaseView.Builder(DeliverAnnouncementActivity.this)
                .focusOn(tv_history)
                .customView(R.layout.glide_for_deliver_announcement, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        view.findViewById(R.id.btn_action_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //                                字符串应保证每个不同的activity都不同
                                GuideCaseView.setShowOnce(DeliverAnnouncementActivity.this,"2");
                                guideCaseView.hide();
                            }
                        });
                    }
                })
                .build();
        if(!GuideCaseView.isShowOnce(DeliverAnnouncementActivity.this,"2")){
            guideCaseView.show();
        }

    }

}
