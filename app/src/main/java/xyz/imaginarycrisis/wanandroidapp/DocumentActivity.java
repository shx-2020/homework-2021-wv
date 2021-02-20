package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;


public class DocumentActivity extends AppCompatActivity implements MyDocumentInterface{
    private List<String> setCookieList;
    private final List<DocumentArticleData> dataList = new ArrayList<>();
    private final int bottom_page = 0;
    RecyclerView recyclerView;
    DocumentRvAdapter adapter;
    RecyclerView.LayoutManager layoutManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        getData();
        initView();
        requestAndAddList(0);
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

    @SuppressLint("HandlerLeak")
    private final Handler documentListHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String responseData = (String) msg.obj;
            dataList.addAll(DocumentArticleData.getArticleDataFromJson(responseData));
            adapter.notifyDataSetChanged();
        }
    };

    private void requestAndAddList(int pg){
        new Thread(
                ()->{
                    try {
                        URL url = new URL("https://www.wanandroid.com/lg/collect/list/"+pg+"/json");
                        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                        for(int i=0;i<setCookieList.size();i++)
                            conn.addRequestProperty("Cookie",setCookieList.get(i));
                        conn.setDoInput(true);
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(8000);
                        conn.setReadTimeout(8000);
                        conn.connect();
                        InputStream in = conn.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        documentListHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(){
        Tools.setupTopBarViews(
                this,"收藏",R.id.document_top_view_bar_layout,
                true,null,true,null,
                R.color.teal_700,false
                );
        initRecyclerView();
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.document_rv);
        adapter = new DocumentRvAdapter(dataList,DocumentActivity.this);
        layoutManager = new LinearLayoutManager(DocumentActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void deleteDocument(int id) {

    }
}