package com.example.xin.dormitory.common;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    //private RadioGroup rg;
    private EditText et_ID;
    private Button bt_confirm;
    private EditText et_pwd;
    private EditText et_confirm;
    private EditText et_name;
    private EditText et_dorm;
    private EditText et_phone;
    private EditText et_nickname;
    private EditText et_belong;
    private Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //rg = findViewById(R.id.rg);
        et_ID = findViewById(R.id.et_ID);
        bt_confirm = findViewById(R.id.bt_confirm);
        et_pwd = findViewById(R.id.et_pwd);
        et_confirm = findViewById(R.id.et_confirm);
        et_dorm = findViewById(R.id.et_dorm);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_nickname = findViewById(R.id.et_nickname);
        et_belong = findViewById(R.id.et_belong);
        //rtv = findViewById(R.id.rtv);
        /*
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(group.findViewById(checkedId).getId()){
                    case R.id.rb_manager:
                        et_ID.setFocusable(false);
                        et_ID.setFocusableInTouchMode(false);
                        break;
                    case R.id.rb_student:
                        et_ID.setFocusableInTouchMode(true);
                        et_ID.setFocusable(true);
                        et_ID.requestFocus();
                        break;
                }
            }
        });
    */

        CheckNotNull("姓名", et_name);
        CheckNotNull("宿舍号",et_dorm);
        CheckNotNull("学号", et_ID);
        CheckNotNull("所属宿舍楼", et_belong);

        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String pwd = et_pwd.getText().toString();
                    if (pwd.length() < 6 || pwd.length() > 10) {
                        Toast.makeText(MyApplication.getContext(), "密码长度应该为6-10个字符", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        et_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String pwd = et_pwd.getText().toString();
                    String confirm = et_confirm.getText().toString();
                    if(!confirm.equals(pwd)){
                        Toast.makeText(MyApplication.getContext(), "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        setListeners();

    }

    /**
     * 检测对应编辑框内容是否为空
     * @param str 提示时对应编辑框的名字
     * @param et 对应编辑框
     */
    private void CheckNotNull(final String str, final EditText et){
        et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if((et.getText().toString()).equals("")){
                    Toast.makeText(MyApplication.getContext(),str + "不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if((et.getText().toString()).equals("")){
                        Toast.makeText(MyApplication.getContext(),str + "不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void setListeners(){
        OnClick onClick = new OnClick();
        bt_confirm.setOnClickListener(onClick);

    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.bt_confirm:
                    String ID = et_ID.getText().toString();
                    String pwd = et_pwd.getText().toString();
                    String name = et_name.getText().toString();
                    String phone = et_phone.getText().toString();
                    String dorm = et_dorm.getText().toString();
                    String nickname = et_nickname.getText().toString();
                    String belong = et_belong.getText().toString();
                    String confirm = et_confirm.getText().toString();
                    myAnimation= AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    if(ID.equals("")||pwd.equals("")||name.equals("")||dorm.equals("")||belong.equals("")||confirm.equals("")) {
                        Toast.makeText(MyApplication.getContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }else if(pwd.equals(confirm)){
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder().add("ID", ID).add("password", pwd).add("dormID", dorm)
                                .add("phone", phone).add("name", name).add("nickname", nickname).add("belong", belong).build();
                        //服务器地址，ip地址需要时常更换
                        String address = HttpUtil.address + "register.php";
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
                                            Toast.makeText(MyApplication.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        Toast.makeText(MyApplication.getContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
