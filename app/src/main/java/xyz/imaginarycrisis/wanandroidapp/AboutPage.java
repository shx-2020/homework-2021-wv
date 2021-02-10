package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
    }
    //隐式调用活动
    public static void actStart(Context context,DecodedLoginData loginData){
        Intent intent = new Intent(context,AboutPage.class);
        context.startActivity(intent);
    }
}