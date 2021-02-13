package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class InnerActivity extends AppCompatActivity {
    //DrawerLayout对象声明
    private Handler mHandlerForAvatar = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            avatarMap = (Bitmap)msg.obj;

            avatarImageButton.setImageBitmap(avatarMap);//正式设置头像按钮的图像
        }
    };//自定义Handler用于接收头像bitmap
    private Bitmap avatarMap;//头像bitmap
    private TextView drawerInfoID;
    private TextView drawerInfoCoin;
    private TextView drawerAboutTv;
    private ImageButton avatarImageButton;
    private TextView drawerNickName;
    private TextView drawerDocumentEntranceTv;

    //json形式的用户资料
    private DecodedLoginData decodedLoginData;

    //ViewPager、BottomNavigationView相关
    private ViewPager mainViewPager;
    private BottomNavigationView bnv;
    private IndexFragment indexFragment;
    private PlaygroundFragment playgroundFragment;
    private SystemFragment systemFragment;
    private NaviFragment naviFragment;
    private ArticleFragment articleFragment;


    //json
    private String data;

    //为了方便，定义一个context指向这个活动的context
    private final Context thisInnerActivityContext = InnerActivity.this;

    //UI对象声明
    private TextView title;

    //onCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        initTopBarViews("玩Android",R.id.inner_top_view_bar_layout);
        getData();
        initDrawerExceptAvatarImage();
        fillAvatarImage();
        initVpAndBnv();
    }
    public static void actStart(Context context,DecodedLoginData loginDataIn){
        Intent intent = new Intent(context,InnerActivity.class);

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
        decodedLoginData = map.get("allData");
    }

    //初始化UI界面
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
                backBtnAct(thisInnerActivityContext);
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
    //初始化drawer（除了头像照片，但还是初始化了头像的其他内容）
    private void initDrawerExceptAvatarImage(){
        drawerDocumentEntranceTv = findViewById(R.id.drawer_document_entrance_tv);
        drawerDocumentEntranceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentActivity.actStart(thisInnerActivityContext);
            }
        });
        drawerNickName = findViewById(R.id.drawer_nickname);
        avatarImageButton = findViewById(R.id.innerAvatarImageButton);
        drawerInfoID = findViewById(R.id.drawer_info_id);
        drawerInfoCoin = findViewById(R.id.drawer_info_coin);
        drawerAboutTv = findViewById(R.id.about_page_tv);
        drawerNickName.setText(decodedLoginData.getNickname());
        avatarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedInfo.actStart(thisInnerActivityContext,decodedLoginData);
            }
        });
        drawerInfoID.setText("ID："+decodedLoginData.getId());
        drawerInfoCoin.setText("硬币数："+decodedLoginData.getCoinCount());
        drawerAboutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutPage.actStart(thisInnerActivityContext,decodedLoginData);
            }
        });
    }

    //加载头像的图像（耗时任务，用了多线程和handler）
    private void fillAvatarImage(){

        Runnable networkGetAvatar = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(decodedLoginData.getIcon());
                    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(10000);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    InputStream in = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    Message messageInRunnable = new Message();
                    messageInRunnable.obj = bitmap;
                    mHandlerForAvatar.handleMessage(messageInRunnable);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };//定义获得bitmap的runnable
        new Thread(networkGetAvatar).start();//开始调用这个runnable的线程
    }

    private void backBtnAct(Context context){
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.ic_baseline_sick_24);
        normalDialog.setTitle("提示").setMessage("您点击了返回按钮，你要……");
        normalDialog.setPositiveButton("重新登录？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.activityStart(thisInnerActivityContext);
                        finish();
                    }
                });
        normalDialog.setNeutralButton("退出程序？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishContext();
                    }
                });
        normalDialog.setNegativeButton("取消？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        normalDialog.show();
    }

    private void finishContext(){
        this.finish();
    }

    private void initVpAndBnv(){
        indexFragment = new IndexFragment();
        playgroundFragment = new PlaygroundFragment();
        systemFragment = new SystemFragment();
        naviFragment = new NaviFragment();
        articleFragment = new ArticleFragment();
        //==========
        mainViewPager = findViewById(R.id.inner_main_viewpager);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bnv.getMenu().getItem(position).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        bnv = findViewById(R.id.bottom_navigation_view);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mainViewPager.setCurrentItem(item.getOrder());
                return true;
            }
        });


        mainViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return indexFragment;
                    case 1:
                        return playgroundFragment;
                    case 2:
                        return systemFragment;
                    case 3:
                        return naviFragment;
                    case 4:
                        return articleFragment;

                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });


    }
}