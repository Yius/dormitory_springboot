package com.example.xin.dormitory.student;

import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

public class AddPostsActivity extends AppCompatActivity {
    private EditText et_addPostsTitle;
    private EditText getEt_addPostsContent;
    private Button bt_addPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);
        Toolbar toolbar_chat = findViewById(R.id.toolbar_add_posts);
        toolbar_chat.setTitle("");
        setSupportActionBar(toolbar_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_addPostsTitle = findViewById(R.id.et_add_posts_title);
        getEt_addPostsContent = findViewById(R.id.et_add_posts_content);
        bt_addPosts = findViewById(R.id.bt_add_posts);

        et_addPostsTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(et_addPostsTitle.getText().toString()).equals("")){
                    bt_addPosts.setEnabled(true);
                    bt_addPosts.setTextColor(Color.BLUE);
                }else{
                    bt_addPosts.setEnabled(false);
                    bt_addPosts.setTextColor(Color.GRAY);
                }
            }
        });

        bt_addPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String PostsDate = formatter.format(new Date(System.currentTimeMillis()));
                SharedPreferences pref1 = getSharedPreferences("data", MODE_PRIVATE);
                String ID = pref1.getString("ID", "");
                SharedPreferences pref2 = getSharedPreferences("data", MODE_PRIVATE);
                String name = pref2.getString("name", "");
                String postsTitle = et_addPostsTitle.getText().toString();
                String postsContent = getEt_addPostsContent.getText().toString();
                OkHttpClient client = new OkHttpClient();
                if(postsTitle.equals("")){
                    Toast.makeText(MyApplication.getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    RequestBody requestBody = new FormBody.Builder().add("LatestReplyTime", PostsDate).add("PostsDate", PostsDate).add("ID", ID).add("name", name).add("postsTitle", postsTitle)
                            .add("postsContent", postsContent).build();
                    //服务器地址，ip地址需要时常更换
                    String address = HttpUtil.address + "addPosts.php";
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
                finish();
            }
        });

    }
}
