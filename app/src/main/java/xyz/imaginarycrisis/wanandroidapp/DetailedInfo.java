package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedInfo extends AppCompatActivity {
    TextView tv;
    Context thisDetailedInfo = DetailedInfo.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        initTopBarViews("详细信息",R.id.detail_info_top_view_bar_layout);
    }
    //隐式调用该活动
    public static void actStart(Context context){
        Intent intent = new Intent(context,DetailedInfo.class);
        context.startActivity(intent);
    }

    private void initTopBarViews(String title, int tarLayoutId){
        Activity thisActivity = this;
        TextView titleTv;
        titleTv = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_title);
        titleTv.setText(title);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
        Button btnBack = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_back_button);
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
}