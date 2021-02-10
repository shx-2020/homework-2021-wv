package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {
    TextView tvTitle;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        initViews();

    }
    //隐式调用活动
    public static void actStart(Context context,DecodedLoginData loginData){
        Intent intent = new Intent(context,AboutPage.class);
        context.startActivity(intent);
    }
    public void initViews(){
        tvTitle=findViewById(R.id.about_page_top_view_bar_layout).findViewById(R.id.top_view_bar_title);
        tvContent=findViewById(R.id.about_page_content);
        tvTitle.setText("关于");
        tvContent.setText("应用名称：玩AndroidApp（imaginarycrisis.xyz版）\n版本：Beta 0.1\n不得用于非法用途或未经授权的商业用途。");
        tvContent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
    }

}