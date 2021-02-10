package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class InnerActivity extends AppCompatActivity {
    //DrawerLayout对象声明
    private Handler mHandlerForAvatar = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            avatarMap = (Bitmap)msg.obj;

            avatarImageButton.setImageBitmap(avatarMap);//正式设置头像按钮的图像
        }
    };//自定义Handler用于接收头像bitmap，前文（误）详见fillAvatarImage()方法
    private Bitmap avatarMap;//头像bitmap
    private TextView drawerInfoID;
    private TextView drawerInfoCoin;
    private Button drawerAboutBtn;
    private ImageButton avatarImageButton;
    private TextView drawerNickName;

    //json形式的用户资料
    private DecodedLoginData decodedLoginData;

    //主页面对象声明
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;


    //json
    private String data;

    //为了方便，定义一个context指向这个活动的context
    private final Context thisInnerActivityContext = InnerActivity.this;

    //UI对象声明
    private TextView title;

    private PagerAdapter mPagerAdapter;
    //onCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);

        initUIViews();
        getAccountData();
        initDrawerExceptAvatarImage();
        fillAvatarImage();
    }
    //初始化UI界面
    private void initUIViews(){
        title = findViewById(R.id.inner_top_view_bar_layout).findViewById(R.id.top_view_bar_title);
        title.setText("首页");
        StatusBarUtils.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
    }
    //隐式启动本活动方法
    public static void activityStart(String responseData, Context context){
        Intent intent = new Intent(context, InnerActivity.class);
        intent.putExtra("data",responseData);
        context.startActivity(intent);
    }
    //获得json以及解码的用户信息的方法
    private void getAccountData(){
        data = getIntent().getStringExtra("data");
        decodedLoginData = DecodedLoginData.spawnDecodedJsonData(data);
    }
    //初始化drawer（除了头像照片，但还是初始化了头像的其他内容）
    private void initDrawerExceptAvatarImage(){
        drawerNickName = findViewById(R.id.drawer_nickname);
        avatarImageButton = findViewById(R.id.innerAvatarImageButton);
        drawerInfoID = findViewById(R.id.drawer_info_id);
        drawerInfoCoin = findViewById(R.id.drawer_info_coin);
        drawerAboutBtn = findViewById(R.id.about_page_btn);
        drawerNickName.setText(decodedLoginData.getNickname());
        avatarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedInfo.actStart(thisInnerActivityContext);
            }
        });
        drawerInfoID.setText("ID："+decodedLoginData.getId());
        drawerInfoCoin.setText("硬币数："+decodedLoginData.getCoinCount());
        drawerAboutBtn.setOnClickListener(new View.OnClickListener() {
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

}