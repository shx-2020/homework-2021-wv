package xyz.imaginarycrisis.wanandroidapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InnerActivity extends AppCompatActivity {

    //===对象声明===
    //数据对象：
    DecodedLoginData loginData;
    //控件对象：
    TextView tvDrawerId,tvDrawerCoinCount,tvDocumentEntrance,tvAboutEntrance,tvNickName;
    ImageButton avatarButton;
    //特别：ViewPager和NavigationView相关
    BottomNavigationView navigation;

    MyViewPager viewPager;
    //设置自定的MyViewPager类型控件，主要是为了实现viewpager不可以直接滑动、只能按钮切换的效果
    //（至于为什么，还要为fragment里面的tabLayout让路，不然会发生滑动手势的冲突）

    List<Fragment> fragmentList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        getData();
        initViews();

    }


    //方法：开始活动
    public static void actStart(Context context, DecodedLoginData loginData){
        Intent intent = new Intent(context,InnerActivity.class);

        //将数据存入map，因为hash map是序列化的数据，可以存入bundle，再将bundle传入活动
        HashMap<String,DecodedLoginData> map = new HashMap<>();
        Bundle bundle = new Bundle();
        map.put("data",loginData);
        bundle.putSerializable("dataInMap",map);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    private void getData(){
        //从bundle获得hashMap，再从hashMap获得loginData
        Intent intent = getIntent();
        Bundle bundle = (Bundle)intent.getExtras();
        HashMap<String,DecodedLoginData> hashMap = (HashMap<String, DecodedLoginData>) bundle.get("dataInMap");
        loginData = (DecodedLoginData) hashMap.get("data");
    }

    //===初始化控件===
    //入口：
    private void initViews(){

        //---顶部导航---
        Tools.setupTopBarViews(this,"玩Android",R.id.inner_top_view_bar_layout,
                true,null,true,null);
        //------
        initTextViews();
        initImageButton();
        initNavigationAndViewPager();
    }
    //初始化TextView控件：
    @SuppressLint("SetTextI18n")
    private void initTextViews(){
        tvDrawerId = findViewById(R.id.drawer_info_id);
        tvDrawerCoinCount = findViewById(R.id.drawer_info_coin);
        tvNickName = findViewById(R.id.drawer_nickname);
        tvAboutEntrance = findViewById(R.id.about_page_tv);
        tvDocumentEntrance = findViewById(R.id.drawer_document_entrance_tv);

        tvNickName.setText("昵称："+loginData.getNickname());
        tvDrawerId.setText("ID："+loginData.getId());
        tvDrawerCoinCount.setText("硬币数："+loginData.getCoinCount());

        tvDocumentEntrance.setOnClickListener(v -> DocumentActivity.actStart(InnerActivity.this));
        tvAboutEntrance.setOnClickListener(v-> AboutPage.actStart(InnerActivity.this));
    }
    //初始化ImageButton：
    private void initImageButton(){
        avatarButton = findViewById(R.id.innerAvatarImageButton);
        avatarButton.setOnClickListener(v->DetailedInfo.actStart(this,loginData));
    }
    //初始化ViewPager和BottomNavigationView：
    private void initNavigationAndViewPager(){

        navigation=(BottomNavigationView) findViewById(R.id.inner_bnv);
        viewPager=(MyViewPager) findViewById(R.id.inner_main_viewpager);

        //初始化添加fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(new IndexFragment());
        fragmentList.add(new PlaygroundFragment());
        fragmentList.add(new SystemFragment());
        fragmentList.add(new NaviFragment());
        fragmentList.add(new ArticleFragment());

        //为viewpager设置自定义Adapter
        MyFragAdapter myFragAdapter = new MyFragAdapter(getSupportFragmentManager(),this,fragmentList);
        viewPager.setAdapter(myFragAdapter);
        viewPager.setSlidingEnable(false);
        //使navigation按钮与viewpager绑定
        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getOrder()){
                case 0:
                    viewPager.setCurrentItem(0);
                    return true;
                case 1:
                    viewPager.setCurrentItem(1);
                    return true;
                case 2:
                    viewPager.setCurrentItem(2);
                    return true;
                case 3:
                    viewPager.setCurrentItem(3);
                    return true;
                case 4:
                    viewPager.setCurrentItem(4);
                    return true;
                default:
                    break;
            }
            return false;
        });
        //viewpager页面更改事件的监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
