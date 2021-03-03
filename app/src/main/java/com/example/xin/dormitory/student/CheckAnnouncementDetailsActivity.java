package com.example.xin.dormitory.student;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.common.Announcement;
import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 因学生的公告详情应该要能看到发布人ID，故另建此类
 */
public class CheckAnnouncementDetailsActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_Atime;
    private TextView tv_content;
    private TextView tv_ID;
    private TextView tv_houseparentName;
    private Announcement announcement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_announcement_details);
        initLayout();
    }

    /**
     * 把布局初始化的代码写在一起
     */
    private void initLayout(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_ID = findViewById(R.id.tv_ID);
        tv_Atime = findViewById(R.id.tv_Atime);
        tv_content = findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());
        tv_title = findViewById(R.id.tv_title);
        tv_houseparentName = findViewById(R.id.tv_houseparentName);
        announcement = (Announcement) getIntent().getSerializableExtra("announcement_data");
        tv_ID.setText(String.valueOf(announcement.getID()));
        tv_title.setText(announcement.getTitle());
        tv_content.setText(announcement.getContent());
        tv_Atime.setText(announcement.getAtime());
        tv_houseparentName.setText(announcement.getHouseparentName());
        //setListeners();

    }


    /**
     * 监听器初始化
     */
//    private void setListeners(){
//        OnClick onClick = new OnClick();
//        tv_houseparentID.setOnClickListener(onClick);
//    }



//    private class OnClick implements View.OnClickListener{
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()){
//
//            }
//        }
//    }

}
