package com.example.xin.dormitory.common;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.houseparent.MainHActivity;
import com.example.xin.dormitory.student.MainSActivity;
import com.example.xin.dormitory.Utility.HttpUtil;

import java.io.IOException;
import okhttp3.*;

public class LoginActivity extends AppCompatActivity {

    private CheckBox cb1;
    private RadioButton houseparent;
    private RadioButton student;
    private EditText et_account,et_pwd;
    private Button bt_register,bt_login;
    private TextView changeHost;
    private Animation myAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //组件初始化
        et_account = findViewById(R.id.et_account);
        et_pwd = findViewById(R.id.et_pwd);
        cb1 = findViewById(R.id.cb_1);
        houseparent = findViewById(R.id.houseparent);
        student = findViewById(R.id.student);
        bt_register = findViewById(R.id.bt_register);
        bt_login = findViewById(R.id.bt_login);
        changeHost = findViewById(R.id.change_host);
        changeHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et_host = new EditText(LoginActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("输入host").setView(et_host)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!(et_host.getText().toString()).equals("")) {
                                    HttpUtil.host = et_host.getText().toString();
                                    HttpUtil.address = "http://"+et_host.getText().toString()+":80/dormitoryPHP/";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "修改成功" + HttpUtil.address, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "修改失败，不能为空", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();

            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb1.isChecked()) {
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        String pwd = intent.getStringExtra("pwd");
        et_account.setText(ID);
        et_pwd.setText(pwd);

        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 监听器初始化
     */
    private void setListeners(){
        OnClick onClick = new OnClick();
        bt_register.setOnClickListener(onClick);
        bt_login.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent ;
            switch(v.getId()) {
                case R.id.bt_register:
                    myAnimation= AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bt_login:
                    myAnimation= AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    loginHelp();
                    break;
            }
        }
    }


    /**
     * 验证账号密码是否一致
     */
    private void loginHelp(){
        String ID = et_account.getText().toString();
        if(student.isChecked()) {
            HttpUtil.ID = ID;
        }else if(houseparent.isChecked()) {
            HttpUtil.HID = ID;
        }
        String pwd = et_pwd.getText().toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("ID",ID).add("password",pwd).build();
        //服务器地址，ip地址需要时常更换
        String address = null;
        if(student.isChecked()) {
            address = HttpUtil.address+"loginS.php";
        }else if(houseparent.isChecked()) {
            address = HttpUtil.address+"loginH.php";
        }
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if(HttpUtil.parseSimpleJSONData(responseData)){
                    //子线程中操作Toast会出现问题，所以用runOnUiThread
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    Intent intent = null;
                    if(student.isChecked()) {
                        intent = new Intent(LoginActivity.this, MainSActivity.class);
                    }else if(houseparent.isChecked()) {
                        intent = new Intent(LoginActivity.this, MainHActivity.class);
                    }
                    startActivity(intent);
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"账号不存在或账号密码不匹配",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
