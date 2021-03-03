package com.example.xin.dormitory.houseparent;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.guidview.GuideCaseQueue;
import com.xuexiang.xui.widget.guidview.GuideCaseView;
import com.xuexiang.xui.widget.guidview.OnViewInflateListener;
import com.xuexiang.xui.widget.tabbar.TabControlView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StayAndDepartActivity extends AppCompatActivity {

    private List<Stay> stayList = new ArrayList<>();
    private List<Depart> departList = new ArrayList<>();
    private StayAdapter stayAdapter;
    private DepartAdapter departAdapter;
    private SmartRefreshLayout smartRefresh;
    private final int CHECK_DEPART_STUDENT = 0;
    private final int CHECK_STAY_STUDENT = 1;
    private int contentMode = CHECK_DEPART_STUDENT;
    private RecyclerView recyclerView;
    private TabControlView mTabControlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_and_depart);

        recyclerView = findViewById(R.id.recycle_view);
        mTabControlView = findViewById(R.id.tcv_select);
        try {
            mTabControlView.setItems(new String[]{"离宿", "留宿"}, new String[]{"0", "1"});
            mTabControlView.setDefaultSelection(contentMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTabControlView.setOnTabSelectionChangedListener(new TabControlView.OnTabSelectionChangedListener() {
            @Override
            public void newSelection(String title, String value) {
                contentMode = Integer.parseInt(value);
                adapterSettingHelper();
                refreshStudentInfo();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        departAdapter = new DepartAdapter(departList);
        stayAdapter = new StayAdapter(stayList);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        smartRefresh = findViewById(R.id.smart_refresh);
        smartRefresh.setRefreshHeader(getRefreshHeader("WaterDropHeader"));
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                adapterSettingHelper();
                refreshStudentInfo();
            }
        });

        adapterSettingHelper();
        refreshStudentInfo();
        firstCheck();
        showTextGuideView();

    }


    /**
     * 初始化显示修理申请
     */
    private void initDepartInfo() {
        departList.clear();

        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("govern", pref.getString("govern", "")).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "departStudentsInfo.php";
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
                        departList.add(new Depart(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //通知数据改变，涉及UI变化，故在runOnUiThread中操作
                            departAdapter.notifyDataSetChanged();
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


    /**
     * 初始化显示的留宿学生
     */
    private void initStayInfo() {
        stayList.clear();

        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("govern", pref.getString("govern", "")).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "stayStudentsInfo.php";
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
                        stayList.add(new Stay(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //通知数据改变，涉及UI变化，故在runOnUiThread中操作
                            stayAdapter.notifyDataSetChanged();
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


    private void refreshStudentInfo() {
        //网络操作耗时，故开子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (contentMode) {
                            case CHECK_DEPART_STUDENT:
                                initDepartInfo();
                                break;
                            case CHECK_STAY_STUDENT:
                                initStayInfo();
                                break;
                        }
                        smartRefresh.finishRefresh();
                    }
                });
            }
        }).start();
    }

    private void adapterSettingHelper() {
        switch (contentMode) {
            case CHECK_DEPART_STUDENT:
                recyclerView.setAdapter(departAdapter);
                break;
            case CHECK_STAY_STUDENT:
                recyclerView.setAdapter(stayAdapter);
                break;
        }
    }

    private RefreshHeader getRefreshHeader(String name) {
        try {
            Class<?> headerClass = Class.forName("com.scwang.smartrefresh.header." + name);
            Constructor<?> constructor = headerClass.getConstructor(Context.class);
            return (RefreshHeader) constructor.newInstance(MyApplication.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private GuideCaseView guideStep2;

    private void showTextGuideView() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        TypedValue tv = new TypedValue();
        StayAndDepartActivity.this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, StayAndDepartActivity.this.getResources().getDisplayMetrics());

        final GuideCaseView guideStep0 = new GuideCaseView.Builder(StayAndDepartActivity.this)
                .title("点击可导出离/留宿学生信息Excel表")
                .focusRectAtPosition(outMetrics.widthPixels - 50, 50, 100, 100)
                .roundRectRadius(60)
                .build();


        final GuideCaseView guideStep1 = new GuideCaseView.Builder(StayAndDepartActivity.this)
                .title("点击查看离宿学生信息")
                .focusRectAtPosition(outMetrics.widthPixels / 4, actionBarHeight + 50, outMetrics.widthPixels / 2, 60)
                .roundRectRadius(60)
                .build();

        guideStep2 = new GuideCaseView.Builder(StayAndDepartActivity.this)
                .focusRectAtPosition(outMetrics.widthPixels * 3 / 4, actionBarHeight + 50, outMetrics.widthPixels / 2, 60)
                .roundRectRadius(60)
                .customView(R.layout.glide_for_stay_and_depart, new OnViewInflateListener() {
                    @Override
                    public void onViewInflated(View view) {
                        view.findViewById(R.id.btn_action_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //                                字符串应保证每个不同的activity都不同
                                GuideCaseView.setShowOnce(StayAndDepartActivity.this,"3");
                                guideStep2.hide();
                            }
                        });
                    }
                })
                .build();

        if(!GuideCaseView.isShowOnce(StayAndDepartActivity.this,"3")){
            new GuideCaseQueue()
                    .add(guideStep0)
                    .add(guideStep1)
                    .add(guideStep2)
                    .show();
        }

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
        getMenuInflater().inflate(R.menu.menu_stay_and_depart, menu);
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
            case R.id.export_depart_excel:
                generateDepartStudentExcel();
                break;
            case R.id.export_stay_excel:
                generateStayStudentExcel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateStayStudentExcel() {
        stayList.clear();
        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("govern", pref.getString("govern", "")).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "stayStudentsInfo.php";
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
                        stayList.add(new Stay(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "开始生成excel文件", Toast.LENGTH_SHORT).show();
                            exportStayStudentExcel(MyApplication.getContext());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void generateDepartStudentExcel() {
        departList.clear();
        SharedPreferences pref = getSharedPreferences("dataH", MODE_PRIVATE);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("govern", pref.getString("govern", "")).build();
        //服务器地址，ip地址需要时常更换
        String address = HttpUtil.address + "departStudentsInfo.php";
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
                        departList.add(new Depart(jsonObject));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "开始生成excel文件", Toast.LENGTH_SHORT).show();
                            exportDepartStudentExcel(MyApplication.getContext());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void exportStayStudentExcel(Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = this.filePath + "/留宿学生信息表.xls";
        String[] title = {"编号", "申请时间", "学生学号", "宿舍号", "开始时间", "结束时间", "联系方式", "学生姓名"};
        ExcelUtil.initExcel(filePath, "留宿学生信息", title);
        ExcelUtil.writeStayListToExcel(stayList, filePath, context);
    }

    private void exportDepartStudentExcel(Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = this.filePath + "/离宿学生信息表.xls";
        String[] title = {"编号", "申请时间", "学生学号", "宿舍号", "离宿原因", "离宿时间", "返校时间" ,"联系方式", "学生姓名"};
        ExcelUtil.initExcel(filePath, "离宿学生信息", title);
        ExcelUtil.writeDepartListToExcel(departList, filePath, context);
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
