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
import android.view.KeyEvent;
import android.view.View.OnClickListener;
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

public class InnerActivity extends AppCompatActivity implements PlaygroundRvAdapter.MyOnClickListener,MyItemInterface,MyPlaygroundInterface{

    /**
     * 属性、数据
     */
    DecodedLoginData loginData;
    private List<String>cookieList = new ArrayList<>();
    /**
     * 控件
     */
    TextView tvDrawerId,tvDrawerCoinCount,tvDocumentEntrance,tvAboutEntrance,tvNickName;
    ImageButton avatarButton;
    BottomNavigationView navigation;
    MyViewPager viewPager;
    List<Fragment> fragmentList;


    /**
     * 回调
     */
    @SuppressLint("HandlerLeak")
    private final Handler pushHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String ArticlePushResponseData = (String)msg.obj;
            doAfterArticlePushRequestDone(ArticlePushResponseData);

        }
    };
    private OnClickListener backBtnListener = v->{
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
    private final OnClickListener refreshBtnListener = v->{
        InnerActivity.actStart(InnerActivity.this,loginData,cookieList);
        finish();
    };


    /**
     * onCreate
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        getData();
        initViews();

    }

    /**
     * 隐式启动活动
     */
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

    /**
     * 从intent中获得数据
     */
    private void getData(){
        //从bundle获得hashMap，再从hashMap获得loginData
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        HashMap<String,DecodedLoginData> hashMap = (HashMap<String, DecodedLoginData>) bundle.get("dataInMap");
        loginData =  hashMap.get("data");
        cookieList = (List<String>)hashMap.get("cookieList");
    }

    /**
     * 初始化控件
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews(){
        Tools.setupTopBarViews(this,"玩Android",R.id.inner_top_view_bar_layout,
                false,backBtnListener,false,refreshBtnListener,
                R.color.sea_green,false);
        //使用自定的义顶部工具栏
        initTextViews();
        initImageButton();
        initNavigationAndViewPager();
    }

    /**
     * TextView
     */
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

        tvDocumentEntrance.setOnClickListener(v -> DocumentActivity.actStart(InnerActivity.this,cookieList));
        tvAboutEntrance.setOnClickListener(v-> AboutPage.actStart(InnerActivity.this));
    }

    /**
     * imageButton
     */
    private void initImageButton(){
        avatarButton = findViewById(R.id.innerAvatarImageButton);
        avatarButton.setOnClickListener(v->DetailedInfo.actStart(this,loginData));
    }

    /**
     * viewpager和bottomNavigationView
     */
    private void initNavigationAndViewPager(){

        navigation=findViewById(R.id.inner_bnv);
        viewPager=findViewById(R.id.inner_main_viewpager);
        NavHelper.disableShiftMode(navigation);

        //初始化添加fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(new IndexFragment());
        fragmentList.add(new PlaygroundFragment());
        fragmentList.add(new TreeFragment());
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
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * PlaygroundFragment中rv控件的adapter调动的接口里的方法
     */

    @Override
    public void requestArticlePush(String title, String link) {
        HashMap<String,String> params = new HashMap<>();
        params.put("title",title);
        params.put("link",link);
        new Thread(
                ()->{
                    try {
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                        URL url = new URL("https://www.wanandroid.com/lg/user_article/add/json");
                        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                        for(int i=0;i<cookieList.size();i++) {
                            connection.addRequestProperty("Cookie", cookieList.get(i));
                        }
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
        ).start();
    }

    /**
     * 接口调用后handler调用的方法，用于处理服务器返回的信息，检查是否提交成功
     */
    private void doAfterArticlePushRequestDone(String jsonData) {
        int errorCode = -99;
        String errorMsg = "默认消息";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("errorCode");
            errorMsg = jsonObject.getString("errorMsg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (errorCode == 0) {
            Toast.makeText(InnerActivity.this, "提交成功", LENGTH_SHORT).show();
        } else {
            Toast.makeText(InnerActivity.this, "提交失败！\n错误" + errorCode + "（" + errorMsg + "）", LENGTH_SHORT).show();
        }
    }
    @SuppressLint("HandlerLeak")
    private final Handler addDocumentHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handleResponseDataOfAddDocumentMethod((String)msg.obj);
        }
    };

    @Override
    public void addInSiteArticle(int id) {
        new Thread(
                () -> {
                    try {
                        URL url = new URL("https://www.wanandroid.com/lg/collect/" + id + "/json");
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        for (int i = 0; i < cookieList.size(); i++) {
                            conn.addRequestProperty("Cookie", cookieList.get(i));
                        }
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setReadTimeout(8000);
                        conn.setConnectTimeout(8000);
                        conn.setRequestMethod("POST");
                        conn.connect();
                        InputStream in = conn.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        addDocumentHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    /**
     * 添加站外文章收藏
     * 写了但是还没添加这个功能
     */
    @Override
    public void addBeyondSiteArticle(String title, String author, String link) {
        HashMap<String,String> params = new HashMap<>();
        params.put("title",title);
        params.put("author",author);
        params.put("link",link);
        new Thread(
                () -> {
                    try {
                        URL url = new URL("https://www.wanandroid.com/lg/collect/add/json");
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        for (int i = 0; i < cookieList.size(); i++) {
                            conn.addRequestProperty("Cookie", cookieList.get(i));
                        }
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setReadTimeout(8000);
                        conn.setConnectTimeout(8000);
                        conn.setRequestMethod("POST");
                        conn.connect();
                        StringBuilder dataToWrite = new StringBuilder();
                        for(String key:params.keySet()){
                            dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                        }
                        OutputStream out = conn.getOutputStream();
                        out.write(dataToWrite.substring(0,dataToWrite.length()-1).getBytes());
                        InputStream in = conn.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        addDocumentHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private void handleResponseDataOfAddDocumentMethod(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            int errorCode = jsonObject.getInt("errorCode");
            String errorMsg = jsonObject.getString("errorMsg");

            if(errorCode == 0){
                Toast.makeText(InnerActivity.this,"收藏添加成功！",LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(
                        InnerActivity.this,"收藏添加失败！\n错误："+errorCode+"（"+errorMsg+"）"
                        ,LENGTH_SHORT).show();
            }
        }catch (Exception e){e.printStackTrace();}
    }


    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void addDocument(int id) {
        new Thread(
                () -> {
                    try {
                        URL url = new URL("https://www.wanandroid.com/lg/collect/" + id + "/json");
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        for (int i = 0; i < cookieList.size(); i++) {
                            conn.addRequestProperty("Cookie", cookieList.get(i));
                        }
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setReadTimeout(8000);
                        conn.setConnectTimeout(8000);
                        conn.setRequestMethod("POST");
                        conn.connect();
                        InputStream in = conn.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        addDocumentHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
}
