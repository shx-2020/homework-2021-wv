package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class DetailedInfo extends AppCompatActivity {
    Context thisDetailedInfo = DetailedInfo.this;
    DecodedLoginData loginData;
    Button button_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        Tools.setupTopBarViews(this,"详细信息",R.id.detail_info_top_view_bar_layout,true,null,true,null);
        getData();
        initializeTextViews();
        initButton();
    }
    //隐式调用该活动
    public static void actStart(Context context,DecodedLoginData loginDataIn){
        Intent intent = new Intent(context,DetailedInfo.class);

        HashMap<String, DecodedLoginData> map = new HashMap<>();
        map.put("allData",loginDataIn);

        Bundle bundle = new Bundle();
        bundle.putSerializable("serializable",map);

        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void getData(){
        Bundle bundle = getIntent().getExtras();
        HashMap<String,DecodedLoginData> map = (HashMap<String, DecodedLoginData>)bundle.getSerializable("serializable");
        loginData = map.get("allData");
    }

    @SuppressLint("SetTextI18n")
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

    private void initButton(){
        button_logout=findViewById(R.id.detailed_pg_logout_btn);
        button_logout.setOnClickListener(v -> {
            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(thisDetailedInfo);
            normalDialog.setIcon(R.drawable.ic_baseline_sick_24);
            normalDialog.setTitle("提示").setMessage("您点击了登出按钮，确定登出？");
            normalDialog.setPositiveButton("确定",
                    (dialog, which) -> logout());
            normalDialog.setNegativeButton("取消", (dialog, which) -> {
                //no action
            });
            normalDialog.show();
        });
    }

    private void logout(){
        clearCookie();
        exit();
    }

    private void clearCookie(){

    }

    private void exit(){
        Intent intent = new Intent(DetailedInfo.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
