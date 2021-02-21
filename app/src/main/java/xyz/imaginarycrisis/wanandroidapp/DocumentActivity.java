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
import android.view.View;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;


public class DocumentActivity extends AppCompatActivity implements MyDocumentInterface{
    private List<String> setCookieList;
    private final List<DocumentArticleData> dataList = new ArrayList<>();
    private int bottom_page = 0;
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
        setupListener();
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
            if(dataList.isEmpty())
                Toast.makeText(DocumentActivity.this,"您暂无收藏……",LENGTH_SHORT).show();
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

    private final View.OnClickListener documentRefreshListener = v -> {
        Toast.makeText(DocumentActivity.this,"刷新页面",LENGTH_SHORT).show();
        actStart(DocumentActivity.this,setCookieList);
        this.finish();
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(){
        Tools.setupTopBarViews(
                this,"收藏",R.id.document_top_view_bar_layout,
                true,null,false,documentRefreshListener,
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


    @SuppressLint("HandlerLeak")
    private final Handler mDeleteDocumentHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String deleteResponse = (String) msg.obj;
            try{
                JSONObject jsonObject = new JSONObject(deleteResponse);
                int errorCode = jsonObject.getInt("errorCode");
                String errorMsg = jsonObject.getString("errorMsg");
                if(errorCode == 0){
                    Toast.makeText(DocumentActivity.this,"删除收藏成功！",LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DocumentActivity.this,"删除收藏失败！\n错误："+errorCode+"（"+errorMsg+"）",LENGTH_SHORT).show();
                }
            }catch (Exception e){e.printStackTrace();}
        }
    };
    @Override
    public void deleteDocument(int id) {
        new Thread(
                ()->{
                    try {
                        URL url = new URL("https://www.wanandroid.com/lg/uncollect_originId/" + id + "/json");
                        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                        conn.setReadTimeout(8000);
                        conn.setConnectTimeout(8000);
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        for(int i=0;i<setCookieList.size();i++){
                            conn.addRequestProperty("Cookie",setCookieList.get(i));
                        }
                        conn.connect();
                        InputStream in = conn.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        mDeleteDocumentHandler.sendMessage(msg);
                    }catch (Exception e){e.printStackTrace();}
                }
        ).start();
    }

    private void setupListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;

                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if(layoutManager instanceof LinearLayoutManager){
                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                }

                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if(lastPosition == Objects.requireNonNull(recyclerView.getLayoutManager()).getItemCount()-1){
                    bottom_page+=1;
                    requestAndAddList(bottom_page);
                }
            }
        });
    }
}