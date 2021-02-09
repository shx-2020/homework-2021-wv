package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InnerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private String data;
    private Intent intent;
    private PagerAdapter mPagerAdapter;

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

    private void initBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

    }
    private void initViewPager(){

    }
}