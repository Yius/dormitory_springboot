package com.example.xin.dormitory.Utility;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.*;

public class HttpUtil {
    public static String host = "47.115.34.14";
    //服务器地址，ip地址需要时常更换
    public static String address="http://47.115.34.14:80/dormitoryPHP/";
    //由于Okhttp3的封装，难以获得php返回的数据，故记录学生ID以及宿管HID，方便之后获取信息的操作,其中学生信息的sharedpreferences文件名为data，宿管为dataH
    //waterCheck和electricityCheck是水电查询的地址，因为sharedPreferences更新可能不够及时，且为了后续判断的方便特地设置的
    public static String waterCheck = null;
    public static String electricityCheck = null;

    public static String ID = null;
    public static String HID = null;

    /**
     * 根据服务器返回的JSON数据判断用户是否存在
     * @param JSONData 返回的JSON数据
     * @return true OR false 表示解析是否成功
     */
    public static boolean parseSimpleJSONData(String JSONData){
        try {
            JSONObject jsonObject = new JSONObject(JSONData);
            /**
             * status代表状态
             * 1代表学生用户名密码匹配
             * 2代表学生注册成功
             * 3代表学生信息修改成功
             * 4代表宿管用户名密码匹配
             * 5代表维修申请提交成功
             * 6代表维修申请状态修改成功
             * 7代表公告发布成功
             * 8代表签到发起成功
             * 9代表签到成功
             * 10代表离宿登记提交成功
             * 11代表留宿登记提交成功
             * 12代表宿管联系方式修改成功
             * 13代表宿管发起新签到成功
             * 14代表学生签到成功
             * 15代表发帖成功
             * 16代表发送信息成功同时更新postsinfo表中的最新回复时间LatestReplyTime成功
             * 17代表删除帖子成功并删除存在sendmessageinfo表中有关该帖的信息成功
             * 18代表设置水电网址成功
             * 19代表头像上传成功
             * 20代表签到记录删除成功
             * -1代表学生不存在
             * -2代表学生密码错误
             * -3代表学生已注册过
             * -4代表注册失败
             * -5代表学生信息修改失败
             * -6代表宿管密码错误
             * -7代表宿管不存在
             * -8代表维修申请提交失败
             * -9代表维修申请状态修改失败
             * -10代表公告发布失败
             * -11代表签到发起失败
             * -12代表签到失败
             * -13代表离宿登记提交失败
             * -14代表留宿登记提交失败
             * -15代表宿管联系方式修改失败
             * -16代表宿管发起新签到失败
             * -17代表学生签到失败，因未能成功签到
             * -18代表学生签到失败，因未能改变signrecord表
             * -19代表学生发帖失败，因未能改变postsinfo表
             * -20代表发信息成功但没更新回复时间
             * -21代表发信息和更新回复时间都失败
             * -22代表删除帖子成功但没删除相应的信息数据
             * -23代表删除帖子及相应数信息据失败
             * -24代表设置水电网址失败
             * -25代表上传图片为空
             * -26代表上传图片失败
             * -27代表签到记录删除失败
             */
            String status = jsonObject.getString("status");
            switch (status){
                case"1": return true;
                case"2": return true;
                case"3": return true;
                case"4": return true;
                case"5": return true;
                case"6": return true;
                case"7": return true;
                case"8": return true;
                case"9": return true;
                case"10":return true;
                case"11":return true;
                case"12":return true;
                case"13":return true;
                case"14":return true;
                case"15":return true;
                case"16":return true;
                case"17":return true;
                case"18":return true;
                case"19":return true;
                case"20":return true;
                default: return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
