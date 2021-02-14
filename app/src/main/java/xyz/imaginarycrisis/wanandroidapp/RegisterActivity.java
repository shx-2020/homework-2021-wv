package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_repassword;
    private Button regButton;
    private final String regURL_string = "https://www.wanandroid.com/user/register";
    private String responseData;
    private final Context thisContext = RegisterActivity.this;

    private MHandler mHandler = new MHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTopBarViews("注册",R.id.register_tvb_layout);
        initViews();
    }

    static void activityStart(Context context){
        Intent intent = new Intent(context,RegisterActivity.class);
        context.startActivity(intent);

    }

    private void initTopBarViews(String title, int tarLayoutId){
        Activity thisActivity = this;
        TextView titleTv;
        titleTv = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_title);
        titleTv.setText(title);
        Tools.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
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
                //刷新方法...
                Toast.makeText(thisActivity,"已刷新",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initViews(){
        initEditTexts();
        initRegButton();
    }

    private void initEditTexts(){
        et_username = findViewById(R.id.reg_et_username);
        et_password = findViewById(R.id.reg_et_password);
        et_repassword = findViewById(R.id.reg_et_repassword);
    }

    private void initRegButton(){
        regButton = findViewById(R.id.reg_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_username.getText().toString().isEmpty()||et_password.getText().toString().isEmpty()
                ||et_repassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "不能留空", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String,String> map= new HashMap<>();
                    map.put("username",et_username.getText().toString());
                    map.put("password",et_password.getText().toString());
                    map.put("repassword",et_password.getText().toString());
                    reg(map);
                }
            }
        });
    }

    private void reg(HashMap<String,String> params) {
        new Thread(
                ()->{
                    try {
                        URL url = new URL(regURL_string);
                        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        connection.setRequestMethod("POST");
                        StringBuilder dataToWrite = new StringBuilder();
                        for(String key:params.keySet()){
                            dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                        }
                        connection.connect();
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(dataToWrite.substring(0,dataToWrite.length()-1).getBytes());
                        InputStream in = connection.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msgChild = new Message();
                        msgChild.obj = responseData;
                        mHandler.handleMessage(msgChild);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private int getErrorCode(){
        int errorCode = -999;
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            errorCode = jsonObject.getInt("errorCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorCode;
    }
    private String getErrorMsg(){
        String errorMsg = "未知";
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            errorMsg = jsonObject.getString("errorMsg");
            if (errorMsg.isEmpty())
                errorMsg = "未知";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorMsg;
    }


    private void toDoAfterHavingGotData() {
        int errCode = getErrorCode();
        String errMsg = getErrorMsg();
        if(errCode==0){
            Toast.makeText(thisContext,"注册成功！",Toast.LENGTH_LONG).show();
            LoginActivity.activityStart(thisContext);
            this.finish();
        }
        else{
            Toast.makeText(thisContext,"注册失败！\n错误："+errMsg,Toast.LENGTH_LONG).show();
        }
    }
    private class MHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            responseData = (String)msg.obj;
            toDoAfterHavingGotData();
        }
    }
}