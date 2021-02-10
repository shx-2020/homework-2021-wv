package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedInfo extends AppCompatActivity {
    TextView tv;
    Context thisDetailedInfo = DetailedInfo.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        initUIViews();
    }
    //隐式调用该活动
    public static void actStart(Context context){
        Intent intent = new Intent(context,DetailedInfo.class);
        context.startActivity(intent);
    }

    private void initUIViews(){
        tv = findViewById(R.id.detail_info_top_view_bar_layout).findViewById(R.id.top_view_bar_title);
        tv.setText("详情");
        StatusBarUtils.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
    }
}