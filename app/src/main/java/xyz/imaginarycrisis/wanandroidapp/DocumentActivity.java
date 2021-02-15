package xyz.imaginarycrisis.wanandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Tools.setupTopBarViews(this,"收藏",R.id.document_top_view_bar_layout,
                true,null,
                true,null);
    }

    public static void actStart(Context context){
        Intent intent = new Intent(context,DocumentActivity.class);
        context.startActivity(intent);
    }


}