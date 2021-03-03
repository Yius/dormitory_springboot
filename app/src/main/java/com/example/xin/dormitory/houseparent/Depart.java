package com.example.xin.dormitory.houseparent;

import android.widget.Toast;

import com.example.xin.dormitory.Utility.MyApplication;

import org.json.JSONObject;

import java.io.Serializable;

public class Depart implements Serializable {

    private int departID;
    private String registerDate;
    private String ID;
    private String dormID;
    private String departCause;
    private String departTime;
    private String backTime;
    private String contact;
    private String belong;
    private String name;

    public Depart(JSONObject jsonObject){
        try {
            this.departID = jsonObject.getInt("departID");
            this.registerDate = jsonObject.getString("registerDate");
            this.ID = jsonObject.getString("ID");
            this.dormID = jsonObject.getString("dormID");
            this.departCause = jsonObject.getString("departCause");
            this.departTime = jsonObject.getString("departTime");
            this.backTime = jsonObject.getString("backTime");
            this.contact = jsonObject.getString("contact");
            this.belong = jsonObject.getString("belong");
            this.name = jsonObject.getString("name");
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(MyApplication.getContext(), "初始化出错", Toast.LENGTH_SHORT).show();
        }
    }

    public int getDepartID() {
        return departID;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public String getID() {
        return ID;
    }

    public String getDormID() {
        return dormID;
    }

    public String getDepartCause() {
        return departCause;
    }

    public String getDepartTime() {
        return departTime;
    }

    public String getBackTime() {
        return backTime;
    }

    public String getContact() {
        return contact;
    }

    public String getBelong() {
        return belong;
    }

    public String getName() {
        return name;
    }
}
