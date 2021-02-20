package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;


public class DocumentActivity extends AppCompatActivity {
    private List<String> setCookieList;


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        getData();
        initView();
    }

    public static void actStart(Context context, List<String> setCookieList){
        Intent intent = new Intent(context,DocumentActivity.class);
        HashMap<String,Object> hashMap = new HashMap<>();
        Bundle bundle = new Bundle();
        hashMap.put("setCookieList",setCookieList);
        bundle.putSerializable("hashMap",hashMap);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void getData(){
        Bundle bundle = getIntent().getExtras();
        HashMap<String,Object> hashMap = (HashMap<String, Object>) bundle.get("hashMap");
        setCookieList = (List<String>) hashMap.get("setCookieList");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(){
        Tools.setupTopBarViews(
                this,"收藏",R.id.document_top_view_bar_layout,
                true,null,true,null,
                R.color.teal_700,false
                );
    }


}