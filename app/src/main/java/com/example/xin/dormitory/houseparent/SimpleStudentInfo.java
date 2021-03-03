package com.example.xin.dormitory.houseparent;

import android.widget.Toast;

import com.example.xin.dormitory.Utility.MyApplication;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 一个简化的学生信息类，用于未签到学生信息
 */
public class SimpleStudentInfo  implements Serializable {

    private String name;
    private String dormID;
    private String phone;

    public SimpleStudentInfo(JSONObject jsonObject){
        try {
            this.dormID = jsonObject.getString("dormID");
            this.name = jsonObject.getString("name");
            this.phone = jsonObject.getString("phone");
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MyApplication.getContext(), "初始化出错", Toast.LENGTH_SHORT).show();
        }
    }

    public String getName() {
        return name;
    }

    public String getDormID() {
        return dormID;
    }

    public String getPhone() {
        return phone;
    }
}
