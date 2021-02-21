package xyz.imaginarycrisis.wanandroidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;




public class TreeFragment extends Fragment {

    /**
     * 数据、属性
     */
    private int cid=0,currentPage=0,priTagIndex = 1, secTagIndex = 1;
    private final List<ArticleData> dataList = new ArrayList<>();
    private boolean firstRun = true;
    private String treeJson,articleJson;
    private TreeArticleRvAdapter articleAdapter;
    private LinearLayoutManager articleLayoutManager;

    /**
     * 控件
     */
    private RecyclerView rvTag1,rvTag2,rvArticle;
    private View view0;

    /**
     * 回调
     */
    @SuppressLint("HandlerLeak")
    private Handler treeHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            treeJson = (String) msg.obj;
            cid = getCidFromTreeData();
            requestTreeArticle();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler articleHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            articleJson = (String)msg.obj;
            List<ArticleData> tDataList = ArticleData.getIndexArticlesDataFromJson(articleJson);
            dataList.addAll(tDataList);
            articleAdapter.notifyDataSetChanged();
        }
    };



    /**
     * ====================
     */
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TreeFragment() {
        // Required empty public constructor
    }

    public static TreeFragment newInstance(String param1, String param2) {
        TreeFragment fragment = new TreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * =====================
     */



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Context context = getContext();
        articleAdapter = new TreeArticleRvAdapter(dataList,context);
        articleLayoutManager = new LinearLayoutManager(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(firstRun)
            view0 = inflater.inflate(R.layout.fragment_tree, container, false);
        return view0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(firstRun) {
            rvTag1 = getView().findViewById(R.id.tree_rv_tag1);
            rvTag2 = getView().findViewById(R.id.tree_rv_tag2);
            rvArticle = getView().findViewById(R.id.tree_rv_article);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firstRun){
            rvArticle.setAdapter(articleAdapter);
            rvArticle.setLayoutManager(articleLayoutManager);
            articleAdapter.notifyDataSetChanged();
            requestTreeData();
            firstRun = false;
        }
    }

    private void requestTreeData(){
        new Thread(
                ()->
                {
                    try {
                        URL url = new URL("https://www.wanandroid.com/tree/json");
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        treeHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private void requestTreeArticle(){
        new Thread(
                ()->
                {
                    try {
                        URL url = new URL("https://www.wanandroid.com/article/list/"+currentPage+"/json?cid="+cid);
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        articleHandler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    private int getCidFromTreeData(){
        try{
            JSONObject jsonObject = new JSONObject(treeJson);
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject dataPriTag = data.getJSONObject(priTagIndex);
            JSONObject dataSecTag = dataPriTag.getJSONArray("children").getJSONObject(secTagIndex);
            int id = dataSecTag.getInt("id");
            return id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}