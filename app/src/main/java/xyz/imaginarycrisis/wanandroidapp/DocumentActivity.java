package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.widget.TextView;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);


    }

    public static void actStart(Context context){
        Intent intent = new Intent(context,DocumentActivity.class);
        context.startActivity(intent);
    }
    private void initUIViews(String title, int tarLayoutId){
        TextView titleTv;
        titleTv = findViewById(tarLayoutId).findViewById(R.id.top_view_bar_title);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.orange_for_top_view_bar);
    }
}