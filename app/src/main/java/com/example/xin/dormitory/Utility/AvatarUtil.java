package com.example.xin.dormitory.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xin.dormitory.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AvatarUtil {
    /**
     * 保存文件
     */
    public static File saveFile(Context context,Bitmap bm,String imgName) {
        String path = context.getExternalCacheDir()+"/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File myIconFile= new File(path + imgName+".png");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myIconFile));
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return myIconFile;
    }


    /**
     * 从本地获取图片
     */
    public static Bitmap getDiskBitmap(Context context,String ID,String type)
    {
        String pathString = context.getExternalCacheDir()+"/"+type+"_"+ID+".png";
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 从网络获取图片設置到imageView,並保存到本地（主要用於其他頁面在本地找不到图片时从网络获取并設置）
     */
    public static void loadAvatar(Context context,ImageView imageView, String ID, String type) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("ID", ID).add("type", type).build();

        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "getAvatar.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "连接失败，无法获取您的头像", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                byte[] check = new byte[1024];
                //判断是否为空
                if (inputStream.read(check) == -1) {
                    //记得更新图片要在UI线程
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //将获取图片保存到本地
                            if(bitmap!=null) {
                                imageView.setImageBitmap(bitmap);
                                saveFile(context, bitmap, type + "_" + ID);
                            }else{
                                Glide.with(context).load(R.drawable.portrait_s).into(imageView);
                            }
//                            view.setImageBitmap(bitmap);
                            //Toast.makeText(context, "网络获取并保存到本地成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    /**
     * 从网络获取图片並保存到本地（用於登陸后的首次获取保存以及作为更新本地图片的保证）
     */
    public static void loadAvatar(String ID, String type) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("ID", ID).add("type", type).build();

        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "getAvatar.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "连接失败，无法获取您的头像", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                byte[] check = new byte[1024];
                //判断是否为空
                if (inputStream.read(check) == -1) {
                    //记得更新图片要在UI线程
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //将获取图片保存到本地
                            if(bitmap!=null) {
                                saveFile(MyApplication.getContext(),bitmap, type + "_" + ID);
                            }
//                            view.setImageBitmap(bitmap);
                            //Toast.makeText(MyApplication.getContext(), "网络获取并保存到本地成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 设置imageView的图片
     */
    public static void setAvatar(Context context,ImageView imageView,String ID,String type) {
        Bitmap bitmap = AvatarUtil.getDiskBitmap(context,ID,type);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(context, "本地获取成功", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            AvatarUtil.loadAvatar(context,imageView,ID,type);

        }
    }

}
