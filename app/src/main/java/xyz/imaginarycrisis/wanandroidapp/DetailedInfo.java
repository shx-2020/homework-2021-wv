package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class DetailedInfo extends AppCompatActivity {
    TextView tv;
    Context thisDetailedInfo = DetailedInfo.this;
    DecodedLoginData loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        initTopBarViews("详细信息",R.id.detail_info_top_view_bar_layout);
        getData();
        initializeTextViews();
    }
    //隐式调用该活动
    public static void actStart(Context context,DecodedLoginData loginDataIn){
        Intent intent = new Intent(context,DetailedInfo.class);

        HashMap<String, DecodedLoginData> map = new HashMap<String,DecodedLoginData>();
        map.put("allData",loginDataIn);

        Bundle bundle = new Bundle();
        bundle.putSerializable("serializable",map);

        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void getData(){
        Bundle bundle = getIntent().getExtras();
        HashMap<String,DecodedLoginData> map = (HashMap<String,DecodedLoginData>)bundle.getSerializable("serializable");
        loginData = map.get("allData");
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
    private void initializeTextViews(){
        TextView tvId,tvUsername,tvNickName,tvPublicName,tvEmail,tvCoinCount,tvAdmin;
        tvId = findViewById(R.id.detail_id);
        tvUsername = findViewById(R.id.detail_username);
        tvNickName= findViewById(R.id.detail_nickname);
        tvPublicName= findViewById(R.id.detail_public_name);
        tvEmail= findViewById(R.id.detail_email);
        tvCoinCount= findViewById(R.id.detail_coin_count);
        tvAdmin = findViewById(R.id.detail_admin);

        tvId.setText("ID："+loginData.getId());
        tvUsername.setText("用户名："+loginData.getUsername());
        tvNickName.setText("昵称："+loginData.getNickname());
        tvPublicName.setText("公共名称："+loginData.getPublicName());
        tvEmail.setText("邮箱："+loginData.getEmail());
        String isAdminToString;
        if(loginData.isAdmin())
            isAdminToString = "是";
        else
            isAdminToString = "否";
        tvAdmin.setText("是否为管理员："+isAdminToString);
        tvCoinCount.setText("硬币数："+loginData.getCoinCount());
    }
}