package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AboutPage extends AppCompatActivity {
    TextView tvTitle;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        initTopBarViews("关于",R.id.about_page_top_view_bar_layout);
        initViews();

    }
    //隐式调用活动
    public static void actStart(Context context,DecodedLoginData loginData){
        Intent intent = new Intent(context,AboutPage.class);
        context.startActivity(intent);
    }
    private void initTopBarViews(String title, int tarLayoutId){
        Activity thisActivity = this;
        TextView titleTv;
        titleTv = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_title);
        titleTv.setText(title);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
        ImageButton btnBack = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_back_button);
        TextView tvRefresh = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_refresh);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Runnable() {
                    @Override
                    public void run() {
                        //刷新方法
                    }
                }.run();
                Toast.makeText(thisActivity,"已刷新",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initViews(){
        tvContent=findViewById(R.id.about_page_content);
        tvContent.setText("应用名称：玩AndroidApp（imaginarycrisis.xyz版）\n版本：Beta 0.1\n不得用于非法用途或未经授权的商业用途。");
        tvContent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

}