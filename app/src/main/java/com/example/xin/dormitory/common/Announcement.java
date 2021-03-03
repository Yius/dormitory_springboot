package com.example.xin.dormitory.common;

import android.widget.Toast;

import com.example.xin.dormitory.Utility.MyApplication;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 公告类
 */
public class Announcement implements Serializable {

    private int ID;
    private String title;
    private String Atime;
    private String houseparentID;
    private String content;
    private String govern;
    private String houseparentName;

    public Announcement(JSONObject jsonObject){
        try {
            this.ID = jsonObject.getInt("ID");
            this.title = jsonObject.getString("title");
            this.Atime = jsonObject.getString("Atime");
            this.houseparentID = jsonObject.getString("houseparentID");
            this.content = jsonObject.getString("content");
            this.govern = jsonObject.getString("govern");
            this.houseparentName = jsonObject.getString("houseparentName");
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(MyApplication.getContext(), "初始化出错", Toast.LENGTH_SHORT).show();
        }
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getAtime() {
        return Atime;
    }

    public String getHouseparentID() {
        return houseparentID;
    }

    public String getContent() {
        return content;
    }

    public String getGovern(){
        return govern;
    }

    public String getHouseparentName(){
        return houseparentName;
    }

}
