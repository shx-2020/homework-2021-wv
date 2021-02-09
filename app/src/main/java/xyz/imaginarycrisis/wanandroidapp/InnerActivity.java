package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InnerActivity extends AppCompatActivity {
    String data;
    Intent intent;
    PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        getAccountData();
    }

    public static void activityStart(String responseData, Context context){
        Intent intent = new Intent(context, InnerActivity.class);
        intent.putExtra("data",responseData);
        context.startActivity(intent);
    }

    private void getAccountData(){
        data = getIntent().getStringExtra("data");
    }

    private void initViews(){
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        }
    }
}