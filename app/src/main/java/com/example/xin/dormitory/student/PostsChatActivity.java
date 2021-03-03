package com.example.xin.dormitory.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.AvatarUtil;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostsChatActivity extends AppCompatActivity {
    private EditText inputText;
    private LinearLayout layout_input_text;
    private Button bt_send;
    private RecyclerView msgRecyclerView;
    private Button bt_show_details;
    private TextView tv_posts_details;
    private MsgAdapter adapter;
    private List<Message> msgList = new ArrayList<>();
    private String PostsID;
    private String SenderID;
    private String SenderName;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    private int status = 1;
    private Drawable ic_show;
    private Drawable ic_hide;
    private Spannable sp;
    private boolean isConnect;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_chat);

        Toolbar toolbar_chat = findViewById(R.id.toolbar_posts_chat);
        toolbar_chat.setTitle("");
        setSupportActionBar(toolbar_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SenderID = pref.getString("ID", "");
        SenderName = pref.getString("name","");

        initMessage();

        inputText = findViewById(R.id.input_text);
        layout_input_text = findViewById(R.id.layout_input_text);
        bt_send = findViewById(R.id.bt_send);
        msgRecyclerView = findViewById(R.id.rv_chat);
        bt_show_details = findViewById(R.id.bt_show_details);
        tv_posts_details = findViewById(R.id.tv_posts_details);
        ic_show = getResources().getDrawable(R.drawable.ic_show);
        ic_show.setBounds(0,0,ic_show.getMinimumWidth(),ic_show.getMinimumHeight());
        ic_hide = getResources().getDrawable(R.drawable.ic_hide);
        ic_hide.setBounds(0,0,ic_hide.getMinimumWidth(),ic_hide.getMinimumHeight());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        connect();

        String Title = getIntent().getStringExtra("Title");
        String Content = getIntent().getStringExtra("Content");
        String details = Title+"\n\n"+Content;
        sp  = new SpannableString(details);
        sp.setSpan(new AbsoluteSizeSpan(25,true),0, Title.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(17,true),details.length()-Content.length(), details.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        layout_input_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.requestFocus();
                showSoftInput(PostsChatActivity.this, inputText);
                handler.sendEmptyMessageDelayed(0,250);
//                msgRecyclerView.scrollToPosition(msgList.size()-1);
            }
        });

        msgRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftInput(PostsChatActivity.this, inputText);
                return false;
            }
        });

        bt_show_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == 1) {
                    bt_show_details.setCompoundDrawables(null,null,ic_show,null);
                    tv_posts_details.setText(sp);
                    tv_posts_details.setVisibility(TextView.VISIBLE);
                    status =0;
                }else{
                    bt_show_details.setCompoundDrawables(null,null,ic_hide,null);
                    tv_posts_details.setVisibility(TextView.GONE);
                    status = 1;
                }
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        if(isConnect){
                            String message = inputText.getText().toString();
                            if(!message.equals("")) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String sendTime = formatter.format(new Date(System.currentTimeMillis()));
                                Message msg = new Message();
                                msg.setId(SenderID);
                                msg.setContent(message);
                                msg.setType(Message.TYPE_SENT);
                                msgList.add(msg);
                                addMessage(sendTime, SenderID, SenderName, PostsID, message);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        inputText.setText("");
                                        adapter.notifyItemInserted(msgList.size() - 1);
                                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                                    }
                                });
                                try {
                                    writer.write(PostsID + " " +SenderID+" "+ SenderName + " " + message + "\n");
                                    writer.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inputText.setText("");
                                    Toast.makeText(PostsChatActivity.this,"连接聊天服务器失败，无法实时收到信息",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();
            }
        });
//        reFresh();
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    @Override
    protected void onDestroy() {
        try {
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 连接socket服务端
     */
    public void connect(){
        //host和port与服务端server.php里的保持一致,看一下server.php文件。。
        //注意要在宝塔面板放行相应端口
        final String host = HttpUtil.host;
        final int port = 8889;
        AsyncTask<Void, String , Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    socket = new Socket(host, port);
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    isConnect = true;
                }catch(UnknownHostException e1){
                    isConnect = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PostsChatActivity.this,"连接聊天服务器失败，无法实时收到信息",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e1.printStackTrace();
                }catch(IOException e){
                    isConnect = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PostsChatActivity.this,"连接聊天服务器失败，无法实时收到信息",Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
                if(isConnect) {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            publishProgress(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if(isConnect) {
                    String[] splited = values[0].split(" ");
                    String recvPostsID = splited[0];
                    if (recvPostsID.equals(PostsID)) {
                        Message msg = new Message();
                        msg.setId(splited[1]);
                        msg.setName(splited[2]);
                        msg.setContent(splited[3]);
                        msg.setType(Message.TYPE_RECEIVED);
                        msgList.add(msg);
                        adapter.notifyItemInserted(msgList.size() - 1);
                        msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    }
                }
                super.onProgressUpdate(values);
            }
        };
        read.execute();

    }

    /**
     *发送消息时存入数据库
     */
    private void addMessage(String SendTime, String SenderID, String SenderName, String PostsID, String Message){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("SendTime", SendTime).add("SenderID", SenderID)
                .add("SenderName", SenderName).add("PostsID",PostsID).add("Message", Message).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "addMessage.php";
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
                if (!HttpUtil.parseSimpleJSONData(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "发送消息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 初始化对应帖子的消息内容
     */
    private void initMessage(){
        msgList.clear();
        try {

            PostsID = getIntent().getStringExtra("PostsID");
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("PostsID",PostsID).build();
            Request request = new Request.Builder().url(HttpUtil.address + "getMessages.php").post(requestBody).build();
            client.newCall(request).enqueue(new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法获取信息",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Message msg = new Message();
                            msg.setId(jsonObject.getString("SenderID"));
                            msg.setImageId(R.drawable.portrait_s);
                            msg.setName(jsonObject.getString("SenderName"));
                            msg.setContent(jsonObject.getString("Message"));
                            if(SenderID.equals(jsonObject.getString("SenderID"))){
                                msg.setType(Message.TYPE_SENT);
                            }else{
                                msg.setType(Message.TYPE_RECEIVED);
                            }
                            msgList.add(msg);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                adapter.notifyItemInserted(msgList.size()-1);
                                msgRecyclerView.scrollToPosition(msgList.size() -1);
                            }
                        });
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

//    private void reFresh(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            initMessage();
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
    class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
        private List<Message> mMsgList;

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView leftMsg;
            TextView rightMsg;
            RelativeLayout leftLayout;
            RelativeLayout rightLayout;
            TextView leftchatterName;
            CircleImageView left_chatter_image;
            CircleImageView right_chatter_image;


            public ViewHolder(View view) {
                super(view);
                leftLayout = view.findViewById(R.id.left_layout);
                rightLayout = view.findViewById(R.id.right_layout);
                leftMsg = view.findViewById(R.id.left_msg);
                rightMsg = view.findViewById(R.id.right_msg);
                leftchatterName = view.findViewById(R.id.left_chatter_name);
                left_chatter_image = view.findViewById(R.id.left_chatter_image);
                right_chatter_image = view.findViewById(R.id.right_chatter_image);

            }
        }

        public MsgAdapter(List<Message> MsgList) {
            this.mMsgList = MsgList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MsgAdapter.ViewHolder holder, int position) {
            final Message msg = mMsgList.get(position);
            if(msg.getType() == Message.TYPE_RECEIVED){
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftMsg.setText(msg.getContent());
                AvatarUtil.setAvatar(MyApplication.getContext(),holder.left_chatter_image,msg.getId(),"student");
                holder.leftchatterName.setText(msg.getName());
                holder.left_chatter_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder().add("ID", msg.getId()).build();
                        String address=HttpUtil.address+"infoS.php";
                        Request request = new Request.Builder().url(address).post(requestBody).build();
                        //匿名内部类实现回调接口
                        client.newCall(request).enqueue(new okhttp3.Callback(){

                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(),"服务器连接失败，无法获取信息",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                try {
                                    Intent intent = new Intent(PostsChatActivity.this,OthersInfoActivity.class);
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    intent.putExtra("contactName",jsonObject.getString("name"));
                                    intent.putExtra("contactID",jsonObject.getString("ID"));
                                    intent.putExtra("contactPhone",jsonObject.getString("phone"));
                                    intent.putExtra("contactNickName",jsonObject.getString("nickname"));
                                    intent.putExtra("contactBelong",jsonObject.getString("belong"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }else if(msg.getType() == Message.TYPE_SENT){
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.leftLayout.setVisibility(View.GONE);
                Log.d("ID:::",msg.getId());
                AvatarUtil.setAvatar(MyApplication.getContext(),holder.right_chatter_image,msg.getId(),"student");
                holder.rightMsg.setText(msg.getContent());
            }
        }

        @Override
        public int getItemCount() {
            return mMsgList.size();
        }
    }


}
