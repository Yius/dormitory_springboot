package com.example.xin.dormitory.Utility;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.xuexiang.xui.XUI;

import java.nio.channels.NetworkChannel;

//这个类用于获得全局context，在难以获得context的情况下有用，同时用于接收广播（尝试了BaseActivity接收，效果更差）
public class MyApplication extends Application {
    private static Context context;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    public void onCreate(){
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        context = getApplicationContext();
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        unregisterReceiver(networkChangeReceiver);
    }


    public static Context getContext(){
        return context;
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        public static final int NETSTATUS_INAVAILABLE = 0;
        public static final int NETSTATUS_WIFI = 1;
        public static final int NETSTATUS_MOBILE = 2;
        public int netStatus = 0;

        public void onReceive(Context context,Intent intent){
            ConnectivityManager connectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);//实例化一个系统服务类
            NetworkInfo mobileNetInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo allNetInfo = connectionManager.getActiveNetworkInfo();//获取可用网络

            if (allNetInfo == null) {   //此时无网络连接
                if (mobileNetInfo != null && (mobileNetInfo.isConnected() || mobileNetInfo.isConnectedOrConnecting())) {
                    netStatus = NETSTATUS_MOBILE;   //使用手机流量
                }
                else if (wifiNetInfo != null && wifiNetInfo.isConnected() || wifiNetInfo.isConnectedOrConnecting()) {
                    netStatus = NETSTATUS_WIFI;     //连接wifi
                }
                else {
                    netStatus = NETSTATUS_INAVAILABLE;  //不可用
                }
            }
            else {      //此时有网络连接
                if (allNetInfo.isConnected() || allNetInfo.isConnectedOrConnecting()) {
                    if (mobileNetInfo.isConnected() || mobileNetInfo.isConnectedOrConnecting()) {
                        netStatus = NETSTATUS_MOBILE;
                    }
                    else {
                        netStatus = NETSTATUS_WIFI;
                    }
                }
                else {
                    netStatus = NETSTATUS_INAVAILABLE;
                }
            }

            if (netStatus == NETSTATUS_INAVAILABLE) {
            Toast.makeText(context, "网络未连接",Toast.LENGTH_SHORT).show();
            }
            else if (netStatus == NETSTATUS_MOBILE) {
            Toast.makeText(context, "网络处于移动网络",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
