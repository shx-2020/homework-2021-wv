package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        initTopBarViews("收藏",R.id.document_top_view_bar_layout);
    }

    public static void actStart(Context context){
        Intent intent = new Intent(context,DocumentActivity.class);
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