package com.example.xin.dormitory.student;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import com.example.xin.dormitory.R;

public class ForegroundService extends Service {
    public ForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent =  new Intent(this,PostsSFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        Notification notification;
        //Android 8.0以上专门要做适配
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID = "ForegroundService";
            NotificationChannel channel = new NotificationChannel(channelID,"前台发帖服务",NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(this,channelID)
                    .setContentTitle("学生宿舍管理系统")
                    .setContentText("有新鲜事？发个帖子聊天吧！")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
        }else{
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("学生宿舍管理系统")
                    .setContentText("有新鲜事？发个帖子聊天吧！")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
        }


        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
