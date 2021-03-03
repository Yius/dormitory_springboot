package com.example.xin.dormitory.houseparent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.example.xin.dormitory.R;

import java.util.List;

public class TipAdapter extends ArrayAdapter {

    private int resourceId;
    private Context mcontext;
    /*参数说明
    Context context：上下文，用getContext（）可获取
    textViewResourceId：自定义的ListView的子项布局 id
    List<Tip> objects：在ListView上的显示数据
    */
    public TipAdapter(Context context, int textViewResourceId, List<Tip> objects){
        super(context,textViewResourceId,objects);
        mcontext = context;
        resourceId = textViewResourceId;
    }

    //重写getView(),这个方法在每个子项被滚到屏幕内都会被调用
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tip tip = (Tip) getItem(position);//获取当前子项的Fruit实例
        /*动态加载自定义的子项布局文件*/
        View view= LayoutInflater.from(mcontext).inflate(resourceId,parent,false);
        //获取子项布局中的控件，并添加内容
        TextView tv = view.findViewById(R.id.tv_tip);
        tv.setText(tip.getName());//添加名字
        return view;
    }

}
