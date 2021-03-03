package com.example.xin.dormitory.houseparent;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.AvatarUtil;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//lyf修改，新增上传头像功能
public class SelfInfoHActivity extends AppCompatActivity {

    private TextView tv_alter,tv_govern,tv_name,tv_ID;
    private EditText et_phone;
    private Button select_photo;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView iv_avatar;
    private RadiusImageView radiusImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info_h);
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        select_photo=findViewById(R.id.select_photo);
        iv_avatar=findViewById(R.id.photo);
        tv_alter = findViewById(R.id.alter);
        et_phone = findViewById(R.id.et_phone);
        tv_name = findViewById(R.id.tv_name);
        tv_govern = findViewById(R.id.tv_govern);
        tv_ID = findViewById(R.id.tv_ID);
        SharedPreferences pref = getSharedPreferences("dataH",MODE_PRIVATE);
        et_phone.setText(pref.getString("phone",""));
        tv_govern.setText(pref.getString("govern",""));
        tv_name.setText(pref.getString("name",""));
        tv_ID.setText(pref.getString("ID",""));
        radiusImageView = findViewById(R.id.photo);
        AvatarUtil.setAvatar(this,radiusImageView,tv_ID.getText().toString(),"houseparent");
        setListeners();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 监听器初始化
     */
    private void setListeners(){
        OnClick onClick = new OnClick();
        tv_alter.setOnClickListener(onClick);
        select_photo.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = new Intent() ;
            switch(v.getId()) {
                case R.id.alter:
                    String phone = et_phone.getText().toString();
                    if(phone.equals(getSharedPreferences("dataH",MODE_PRIVATE).getString("phone",""))){
                        Toast.makeText(MyApplication.getContext(),"你好像没有变更联系方式哦",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPreferences.Editor editor = getSharedPreferences("dataH",MODE_PRIVATE).edit();
                    editor.putString("phone",phone);
                    editor.apply();

                    //更新数据库中用户的nickname和phone
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder().add("ID",HttpUtil.HID).add("phone",phone).build();
                    //服务器地址，ip地址需要时常更换
                    String address=HttpUtil.address+"alterH.php";
                    Request request = new Request.Builder().url(address).post(requestBody).build();
                    //匿名内部类实现回调接口
                    client.newCall(request).enqueue(new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyApplication.getContext(),"服务器连接失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            //子线程中操作Toast会出现问题，所以用runOnUiThread
                            if(HttpUtil.parseSimpleJSONData(responseData)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(),"修改失败",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                    break;
                case R.id.select_photo:
                    new BottomSheet.BottomListSheetBuilder(SelfInfoHActivity.this)
                            .setTitle("上传头像")
                            .addItem("拍照")
                            .addItem("从图库中选择")
                            .setIsCenter(true)
                            .setOnSheetItemClickListener(new BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(BottomSheet dialog, View itemView, int position, String tag) {
                                    dialog.dismiss();
                                    if (position == 0) {
                                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                                        try {
                                            if (outputImage.exists()) {
                                                outputImage.delete();
                                            }
                                            outputImage.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (Build.VERSION.SDK_INT < 24) {
                                            tempUri = Uri.fromFile(outputImage);
                                        } else {
                                            tempUri = FileProvider.getUriForFile(SelfInfoHActivity.this, "com.example.dormitory.fileprovider", outputImage);
                                        }
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                        startActivityForResult(intent, TAKE_PICTURE);
                                    }
                                    else if(position==1){
                                        Intent openAlbumIntent = new Intent();
                                        if (Build.VERSION.SDK_INT < 19) {
                                            openAlbumIntent.setAction(Intent.ACTION_GET_CONTENT);
                                            openAlbumIntent.setType("image/*");
                                        }
                                        else {
                                            openAlbumIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                                        }
                                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                                    }
                                }
                            })
                            .build()
                            .show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri,TAKE_PICTURE); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData(),CHOOSE_PICTURE); // 开始对图片进行裁剪处理
                    break;

                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    protected void startPhotoZoom(Uri uri,int requestCode) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        System.out.println("uri"+tempUri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if(requestCode==TAKE_PICTURE){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            int width = photo.getWidth();

            int height = photo.getHeight();
            int newWidth = 450;
            int newHeight = 450;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newphoto = Bitmap.createBitmap(photo, 0, 0, width, height, matrix, true);//处理图片大小
            iv_avatar.setImageBitmap(newphoto);
            uploadNewAvatar(newphoto);
        }
    }

    private void uploadNewAvatar(Bitmap bitmap){
        OkHttpClient client = new OkHttpClient();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] datas = baos.toByteArray();
        // 设置文件以及文件上传类型封装
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), datas);

        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        // 文件上传的请求体封装
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", "houseparent_"+pref.getString("ID","")+".png", requestBody)
                .build();

        Request request = new Request.Builder()
                .url(HttpUtil.address + "uploadAvatar.php")
                .post(multipartBody)
                .build();

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
                if(!HttpUtil.parseSimpleJSONData(responseData)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "图片上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "图片上传成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //上传图片成功后保存图片到本地
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AvatarUtil.saveFile(SelfInfoHActivity.this,bitmap,"houseparent_"+pref.getString("ID",""));
                        }
                    });
                }
            }
        });
    }


}
