package xyz.imaginarycrisis.wanandroidapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;

public class InnerActivity extends AppCompatActivity implements PlaygroundRvAdapter.MyOnClickListener{

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

    private List<String>cookieList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private final Handler pushHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String ArticlePushResponseData = (String)msg.obj;
            doAfterArticlePushRequestDone(ArticlePushResponseData);

        }
    };

    View.OnClickListener backBtnListener = v->{
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.ic_baseline_sick_24);
        normalDialog.setTitle("提示").setMessage("您点击了返回按钮，你要？");
        normalDialog.setPositiveButton("退出", (dialog, which) -> finish());
        normalDialog.setNegativeButton("取消", (dialog, which) -> {
            //no action
        });
        normalDialog.setNeutralButton("登出", (dialog, which) -> {
            LoginActivity.activityStart(InnerActivity.this);
            finish();
        });
        normalDialog.show();
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        getData();
        initViews();

    }


    //方法：开始活动
    public static void actStart(Context context, DecodedLoginData loginData,List<String> cookieList){
        Intent intent = new Intent(context,InnerActivity.class);

        //将数据存入map，因为hash map是序列化的数据，可以存入bundle，再将bundle传入活动
        HashMap<String,Object> map = new HashMap<>();
        Bundle bundle = new Bundle();
        map.put("data",loginData);
        map.put("cookieList",cookieList);
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
        cookieList = (List<String>)hashMap.get("cookieList");
    }

    //===初始化控件===
    //入口：
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews(){

        //---顶部导航---
        Tools.setupTopBarViews(this,"玩Android",R.id.inner_top_view_bar_layout,
                false,backBtnListener,true,null,
                R.color.sea_green,false);
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


    @Override
    public void requestArticlePush(String title, String link) {
        HashMap<String,String> params = new HashMap<>();
        params.put("title",title);
        params.put("link",link);
        String oneLineCookie = "";
        for(int i=0;i<cookieList.size();i++){
            oneLineCookie+=cookieList.get(i);
        }

        String finalOneLineCookie = oneLineCookie;
        new Thread(
                ()->{
                    try {
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                        URL url = new URL("https://www.wanandroid.com/lg/user_article/add/json");
                        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                        connection.setRequestProperty("Cookie", finalOneLineCookie);
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        StringBuilder dataToWrite = new StringBuilder();
                        for(String key:params.keySet()){
                            dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                        }
                        connection.connect();
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(dataToWrite.substring(0,dataToWrite.length()-1).getBytes());
                        InputStream in = connection.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        pushHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sys","connection error");
                    }
                }
        ).run();
    }
    private void doAfterArticlePushRequestDone(String jsonData){
        int errorCode = -99;
        String errorMsg = "默认消息";
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("errorCode");
            errorMsg = jsonObject.getString("errorMsg");
        }catch (Exception e){e.printStackTrace();}
        if(errorCode == 0){
            Toast.makeText(InnerActivity.this,"提交成功", LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(InnerActivity.this,"提交失败！\n错误"+errorCode+"（"+errorMsg+"）",LENGTH_SHORT).show();
        }
    }

}
