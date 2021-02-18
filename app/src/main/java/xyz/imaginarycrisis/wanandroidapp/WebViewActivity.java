package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView; 

public class WebViewActivity extends AppCompatActivity {
    private String url,title;
    private WebView webView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getData();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(){
        Tools.setupTopBarViews(this,title,R.id.web_view_tvb,true,
                null,true,null,R.color.teal_200,
                false);
        initWebView();
    }

    public static void actStart(Context context, String url, String title){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    private void getData(){
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(){
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.loadUrl(url);
    }
}