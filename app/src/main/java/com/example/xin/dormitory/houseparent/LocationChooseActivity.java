package com.example.xin.dormitory.houseparent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.xin.dormitory.R;
import com.example.xin.dormitory.Utility.HttpUtil;
import com.example.xin.dormitory.common.PoiOverlay;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.xin.dormitory.Utility.MyApplication.getContext;

public class LocationChooseActivity extends AppCompatActivity implements Inputtips.InputtipsListener {

    private MapView mMapView = null;
    private EditText editText = null;
    private ListView listView = null;
    private AMap aMap = null;
    private TipAdapter aAdapter = null;
    private Button bt_ok;
    //输入提示点
    private Tip tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_choose);
        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mMapView.getMap();

        editText = findViewById(R.id.et_place);
        bt_ok = findViewById(R.id.bt_ok);
        listView = findViewById(R.id.lv);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.bt_ok){
                    if(tip.getPoint() == null){
                        Toast.makeText(LocationChooseActivity.this, "不是一个确切的地址", Toast.LENGTH_SHORT).show();
                    }else{
                        postNewSignHelp(getIntent().getStringExtra("title"),tip.getPoint().getLatitude(),tip.getPoint().getLongitude(),tip.getAddress());
                        finish();
                    }
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //如果改变了，并且文本长度>0
                if (s.toString().length() > 0) {
                    showLocationTips(s.toString());
                }
            }
        });

//        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onGetInputtips(final List<Tip> tipList, int rCode) {
        //通过tipList获取Tip信息
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            //不要用getApplicationContext()
            aAdapter = new TipAdapter(
                    LocationChooseActivity.this, R.layout.layout_tip, tipList);
            listView.setAdapter(aAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    tip = tipList.get(position);
                    PoiItem poiItem = new PoiItem(tip.getPoiID(), tip.getPoint(), tip.getName(), "DefaultMarker");
                    List<PoiItem> list = new ArrayList<>();
                    list.add(poiItem);
                    if (tip.getPoint() == null) {
                        Toast.makeText(LocationChooseActivity.this, "不是一个确切的地址", Toast.LENGTH_SHORT).show();
                    } else {
//                        LatLng latLng = new LatLng(tem.getPoint().getLatitude(),tem.getPoint().getLongitude());
//                        aMap.addMarker(new MarkerOptions().position(latLng).title(tem.getName()).snippet("DefaultMarker"));
                        editText.setText(tip.getName());
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, list);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    }
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //通知数据改变，涉及UI变化，故在runOnUiThread中操作
                    aAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(LocationChooseActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLocationTips(String s){
        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
        InputtipsQuery inputquery = new InputtipsQuery(s, null);
        Inputtips inputTips = new Inputtips(LocationChooseActivity.this, inputquery);
        inputTips.setInputtipsListener(LocationChooseActivity.this);
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 发起新签到所进行的网络交互的协助函数
     * @param title 签到标题
     */
    private void postNewSignHelp(String title,double latitude,double longitude,String detailAddress){
        SharedPreferences pref = getSharedPreferences("dataH",MODE_PRIVATE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Rtime = formatter.format(new Date(System.currentTimeMillis()));
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("Rtime",Rtime).add("houseparentID",pref.getString("ID",""))
                .add("title",title).add("govern",pref.getString("govern","")).add("latitude",String.valueOf(latitude))
                .add("longitude",String.valueOf(longitude)).add("detailAddress",detailAddress).add("houseparentName",pref.getString("name","")).build();
        //服务器地址，ip地址需要时常更换
        String address=HttpUtil.address+"createNewSign.php";
        Request request = new Request.Builder().url(address).post(requestBody).build();
        //匿名内部类实现回调接口
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"服务器连接失败，无法发布信息",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                //子线程中操作Toast会出现问题，所以用runOnUiThread
                if (HttpUtil.parseSimpleJSONData(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "发起成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "发起失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
