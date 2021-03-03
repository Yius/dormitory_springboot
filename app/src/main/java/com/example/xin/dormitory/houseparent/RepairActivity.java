package com.example.xin.dormitory.houseparent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.ExcelUtil;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.Utility.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.guidview.GuideCaseQueue;
import com.xuexiang.xui.widget.guidview.GuideCaseView;
import com.xuexiang.xui.widget.guidview.OnViewInflateListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RepairActivity extends AppCompatActivity {

    private List<Repair> repairList = new ArrayList<>();
    private RepairAdapter adapter;
    private SmartRefreshLayout smartRefresh;

    //显示全部则为"2",已处理为"1"，未处理为"0"。
    private String which = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        initRepair();
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RepairAdapter(repairList);
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        TitleBar titleBar = findViewById(R.id.titlebar);
//        titleBar.addAction(new TitleBar.ImageAction(R.drawable.ic_navigation_more) {
//            @Override
//            public void performAction(View view) {
//                simulateKey(KeyEvent.KEYCODE_MENU);
//            }
//        });

        smartRefresh = findViewById(R.id.smart_refresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshRepairs();
            }
        });
        firstCheck();

        showTextGuideView();
    }

    /**
     * 初始化显示修理申请
     */
    private void initRepair() {
        repairList.clear();

        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("which", which).add("govern", pref.getString("govern", "")).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "getRepairInfo.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "服务器连接失败，无法获取信息", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        repairList.add(new Repair(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //通知数据改变，涉及UI变化，故在runOnUiThread中操作
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MyApplication.getContext(), "数据加载完成", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }


    private void refreshRepairs() {
        //网络操作耗时，故开子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRepair();
                        smartRefresh.finishRefresh();
                    }
                });
            }
        }).start();
    }


    /**
     * 菜单设置
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载 布局实现
        getMenuInflater().inflate(R.menu.menu_repair_h, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单的选择事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_handled:
                which = "1";
                smartRefresh.autoRefresh();
                break;
            case R.id.show_unhandled:
                which = "0";
                smartRefresh.autoRefresh();
                break;
            case R.id.show_all:
                which = "2";
                smartRefresh.autoRefresh();
                break;
            case R.id.export_excel:
                generateExcel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    public static void simulateKey(final int KeyCode) {
//        new Thread() {
//            public void run() {
//                try {
//                    Instrumentation inst = new Instrumentation();
//                    inst.sendKeyDownUpSync(KeyCode);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }


    private GuideCaseView guideCaseView;

    private void showTextGuideView() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        guideCaseView = new GuideCaseView.Builder(RepairActivity.this)
                .focusRectAtPosition(outMetrics.widthPixels - 50, 50, 100, 100)
                .roundRectRadius(60)
                .customView(R.layout.glide_for_repair_activity, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        view.findViewById(R.id.btn_action_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                字符串应保证每个不同的activity都不同
                                GuideCaseView.setShowOnce(RepairActivity.this, "1");
                                guideCaseView.hide();
                            }
                        });
                    }
                })
                .build();
        if (!GuideCaseView.isShowOnce(RepairActivity.this, "1")) {
            guideCaseView.show();
        }
    }

    private void generateExcel() {
        //先清空以防止导出的数据重复
        repairList.clear();
        SharedPreferences preff = getSharedPreferences("dataH", MODE_PRIVATE);
        OkHttpClient clientt = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("which", "2").add("govern", preff.getString("govern", "")).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "getRepairInfo.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        clientt.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "服务器连接失败，无法获取信息", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        repairList.add(new Repair(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "开始生成excel文件", Toast.LENGTH_SHORT).show();
                            exportExcel(MyApplication.getContext());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void exportExcel(Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = this.filePath + "/维修申请表.xls";
        String[] title = {"申请编号", "申请时间", "申请宿舍", "维修物品", "损坏原因", "详情描述", "联系方式", "其他备注", "处理状态"};
        ExcelUtil.initExcel(filePath, "维修申请", title);
        ExcelUtil.writeRepairListToExcel(repairList, filePath, context);
    }


    private String filePath = Environment.getExternalStorageDirectory() + "/dormitory";

    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码


    public void firstCheck() {
        mPermissionList.clear();//清空没有通过的权限
        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MyApplication.getContext(), "缺少必要的权限！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

}
