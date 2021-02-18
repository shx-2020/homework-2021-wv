package xyz.imaginarycrisis.wanandroidapp;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {
    TextView tvContent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        Tools.setupTopBarViews(this,"关于",R.id.about_page_top_view_bar_layout,
                true,null,true,
                null, R.color.sea_green,false);
        initViews();

    }
    //隐式调用活动
    public static void actStart(Context context){
        Intent intent = new Intent(context,AboutPage.class);
        context.startActivity(intent);
    }


    public void initViews(){
        tvContent=findViewById(R.id.about_page_content);
        tvContent.setText("应用名称：玩AndroidApp（imaginarycrisis开发，基于玩Android的开发API）\n版本：Beta 0.1\n不得用于非法用途或未经授权的商业用途。");
        tvContent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }



}