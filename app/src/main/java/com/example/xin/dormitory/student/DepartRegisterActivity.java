package com.example.xin.dormitory.student;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepartRegisterActivity extends AppCompatActivity {
    private Button departTime;
    private TextView tv_departTime;
    private Button backTime;
    private TextView tv_backTime;
    private Button depart_register;
    private EditText et_departCause;
    private EditText et_contact1;
    private TextView tv_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depart_register);
        departTime = findViewById(R.id.departTime);
        tv_departTime = findViewById(R.id.tv_departTime);
        backTime = findViewById(R.id.backTime);
        tv_backTime = findViewById(R.id.tv_backTime);
        depart_register = findViewById(R.id.depart_register);
        et_departCause = findViewById(R.id.et_departCause);
        et_contact1 = findViewById(R.id.et_contact1);
        tv_id= findViewById(R.id.tv_id);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        tv_id.setText(pref.getString("ID",""));
        Toolbar toolbar_depart = findViewById(R.id.toolbar);
        toolbar_depart.setTitle("");
        setSupportActionBar(toolbar_depart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListeners();
    }
    private void setListeners() {
        OnClick onClick = new OnClick();
        departTime.setOnClickListener(onClick);
        backTime.setOnClickListener(onClick);
        depart_register.setOnClickListener(onClick);
    }
    private class OnClick implements View.OnClickListener{
        Date d_departTime;
        Date d_backTime;
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.departTime:
                    final DatePickerDialog datePickerDialog1 = new DatePickerDialog(DepartRegisterActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        }
                    },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    datePickerDialog1.setButton(DialogInterface.BUTTON_POSITIVE, "下一步", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new TimePickerDialog(DepartRegisterActivity.this,TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    int year = datePickerDialog1.getDatePicker().getYear()-1900;
                                    int month = datePickerDialog1.getDatePicker().getMonth();
                                    int dayOfMonth= datePickerDialog1.getDatePicker().getDayOfMonth();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    d_departTime = new Date(year,month,dayOfMonth,hourOfDay,minute);
                                    String departTime = formatter.format(d_departTime);
                                    tv_departTime.setText(departTime);
                                }
                            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),true).show();
                        }
                    });
                    datePickerDialog1.show();
                    break;
                case R.id.backTime:
                    final DatePickerDialog datePickerDialog2 = new DatePickerDialog(DepartRegisterActivity.this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        }
                    },Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                    datePickerDialog2.setButton(DialogInterface.BUTTON_POSITIVE, "下一步", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new TimePickerDialog(DepartRegisterActivity.this,TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    int year = datePickerDialog2.getDatePicker().getYear()-1900;
                                    int month = datePickerDialog2.getDatePicker().getMonth();
                                    int dayOfMonth= datePickerDialog2.getDatePicker().getDayOfMonth();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    d_backTime = new Date(year,month,dayOfMonth,hourOfDay,minute);
                                    String backTime = formatter.format(d_backTime);
                                    tv_backTime.setText(backTime);
                                }
                            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),true).show();
                        }
                    });
                    datePickerDialog2.show();
                    break;
                case R.id.depart_register:
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String registerDate = formatter.format(new Date(System.currentTimeMillis()));
                    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                    String ID = pref.getString("ID","");
                    String dormID = pref.getString("dormID", "");
                    String departCause = et_departCause.getText().toString();
                    String departTime = tv_departTime.getText().toString();
                    String backTime = tv_backTime.getText().toString();
                    String contact = et_contact1.getText().toString();
                    String belong = pref.getString("belong", "");
                    String name = pref.getString("name","");
                    OkHttpClient client = new OkHttpClient();
                    if(departCause.equals("")||departTime.equals("")||backTime.equals("")||contact.equals("")){
                        Toast.makeText(MyApplication.getContext(), "所有内容都为必填", Toast.LENGTH_SHORT).show();
                    }else if(d_departTime.after(d_backTime)){
                        Toast.makeText(MyApplication.getContext(), "离宿时间不能超过回宿时间", Toast.LENGTH_SHORT).show();
                    }else{
                        RequestBody requestBody = new FormBody.Builder().add("registerDate", registerDate).add("ID",ID)
                                .add("dormID", dormID).add("departCause", departCause).add("departTime", departTime)
                                .add("backTime", backTime).add("contact",contact).add("belong",belong).add("name",name).build();
                        //服务器地址，ip地址需要时常更换
                        String address = HttpUtil.address + "departRegister.php";
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
            }
        }
    }
}
