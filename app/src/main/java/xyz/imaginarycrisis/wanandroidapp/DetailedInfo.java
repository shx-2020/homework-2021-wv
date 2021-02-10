package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DetailedInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);
    }
    //隐式调用该活动
    public static void actStart(Context context){
        Intent intent = new Intent(context,DetailedInfo.class);
        context.startActivity(intent);
    }
}