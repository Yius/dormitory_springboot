package com.example.xin.dormitory.houseparent;

import android.widget.Toast;

import com.example.xin.dormitory.Utility.MyApplication;

import org.json.JSONObject;

import java.io.Serializable;

public class Stay implements Serializable {

    private int stayID;
    private String registerDate;
    private String ID;
    private String dormID;
    private String startDate;
    private String endDate;
    private String contact;
    private String belong;
    private String name;

    public Stay(JSONObject jsonObject){
        try {
            this.stayID = jsonObject.getInt("stayID");
            this.registerDate = jsonObject.getString("registerDate");
            this.ID = jsonObject.getString("ID");
            this.dormID = jsonObject.getString("dormID");
            this.startDate = jsonObject.getString("startDate");
            this.endDate = jsonObject.getString("endDate");
            this.contact = jsonObject.getString("contact");
            this.belong = jsonObject.getString("belong");
            this.name = jsonObject.getString("name");
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(MyApplication.getContext(), "初始化出错", Toast.LENGTH_SHORT).show();
        }
    }

    public int getStayID() {
        return stayID;
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

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
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
