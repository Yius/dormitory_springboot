package com.example.xin.dormitory.student;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.xin.dormitory.R;
//lyf修改，查看通知
public class CheckNoticesActivity extends AppCompatActivity {

    private Button bt_announcement;
    private Button bt_sign;
    private Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_notices);
        initLayout();
    }

    private void initLayout(){
        bt_announcement = findViewById(R.id.bt_announcement);
        bt_sign = findViewById(R.id.bt_sign);
        Toolbar toolbar = findViewById(R.id.toolbar_check);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick = new OnClick();
        bt_announcement.setOnClickListener(onClick);
        bt_sign.setOnClickListener(onClick);
    }


    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v){
            Intent intent = null;
            switch(v.getId()) {
                case R.id.bt_announcement:
                    myAnimation= AnimationUtils.loadAnimation(CheckNoticesActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    intent = new Intent(CheckNoticesActivity.this,CheckAnnouncementNoticesActivity.class);
                    break;
                case R.id.bt_sign:
                    myAnimation= AnimationUtils.loadAnimation(CheckNoticesActivity.this, R.anim.anim_alpha);
                    v.startAnimation(myAnimation);
                    intent = new Intent(CheckNoticesActivity.this,CheckSignNoticesActivity.class);
                    break;
                default:
                    break;
            }
            startActivity(intent);
        }
    }
}
