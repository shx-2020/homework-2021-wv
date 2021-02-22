package xyz.imaginarycrisis.wanandroidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private int cid=0,currentPage=0,priTagIndex = 0, secTagIndex = 0;
    private List<ArticleData> articleDataList = new ArrayList<>();
    private List<String> priNameList = new ArrayList<>();
    private List<String> secNameList = new ArrayList<>();
    private boolean firstRun = true;
    private String treeJson,articleJson;
    private TreeArticleRvAdapter articleAdapter;
    private LinearLayoutManager articleLayoutManager,priLayManager,secLayManager;
    private PrimaryRvAdapter priAdapter;
    private SecondaryRvAdapter secAdapter;
    private Context context;
    private MyInnerActInterface myInnerActInterface;

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
            getPriTagNames();
            getSecTagNames();
            requestTreeArticle();
            priAdapter.notifyDataSetChanged();
            secAdapter.notifyDataSetChanged();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler articleHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            articleJson = (String)msg.obj;
            List<ArticleData> tDataList = ArticleData.getIndexArticlesDataFromJson(articleJson);
            articleDataList.addAll(tDataList);
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
        if(firstRun) {


            context = getContext();
            myInnerActInterface = (MyInnerActInterface)context;
            sendFragment();

            articleAdapter = new TreeArticleRvAdapter(articleDataList, context);
            articleLayoutManager = new LinearLayoutManager(context);

            priAdapter = new PrimaryRvAdapter(priNameList, context);
            priLayManager = new GridLayoutManager(context,2);
            priLayManager.setOrientation(GridLayoutManager.HORIZONTAL);

            secAdapter = new SecondaryRvAdapter(secNameList, context);
            secLayManager = new GridLayoutManager(context,2);
            secLayManager.setOrientation(GridLayoutManager.HORIZONTAL);
        }
    }

    /**
     * onCreateView
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(firstRun)
            view0 = inflater.inflate(R.layout.fragment_tree, container, false);
        return view0;
    }

    /**
     * onViewCreated
     * 当该fragment的view创建后，绑定控件
     */

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

            rvTag1.setAdapter(priAdapter);
            rvTag1.setLayoutManager(priLayManager);
            priAdapter.notifyDataSetChanged();

            rvTag2.setAdapter(secAdapter);
            rvTag2.setLayoutManager(secLayManager);
            secAdapter.notifyDataSetChanged();

            requestTreeData();
            firstRun = false;
        }
    }

    /**
     * 请求获得体系列表
     */

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

    /**
     * 请求获得特定标签下的文章
     */

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

    /**
     * 获得特定文章的id
     */

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

    private void getPriTagNames(){
        try{
            JSONObject jsonObject = new JSONObject(treeJson);
            JSONArray data = jsonObject.getJSONArray("data");
            for(int i=0;i<data.length();i++){
                JSONObject tJsonObject = data.getJSONObject(i);
                String name = tJsonObject.getString("name");
                priNameList.add(name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getSecTagNames(){
        try{
            JSONObject jsonObject = new JSONObject(treeJson);
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject secTagsJsonObj = data.getJSONObject(priTagIndex);
            JSONArray secTagsArray = secTagsJsonObj.getJSONArray("children");
            for(int i=0;i<secTagsJsonObj.length();i++){
                secNameList.add(secTagsArray.getJSONObject(i).getString("name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCurrentTag(int priTagIndex,int secTagIndex){
        if((priTagIndex==this.priTagIndex)&&(secTagIndex==this.secTagIndex)){
            return;
        }else {

            this.priTagIndex = priTagIndex;
            this.secTagIndex = secTagIndex;

            this.cid = getCidFromTreeData();

            secNameList.clear();
            getSecTagNames();
            secAdapter.notifyDataSetChanged();
            articleDataList.clear();
            requestTreeArticle();
        }
    }

    public void sendFragment(){
        myInnerActInterface.getTreeFragment(this);
    }

    public int getPriTagIndex() {
        return priTagIndex;
    }

    public int getSecTagIndex() {
        return secTagIndex;
    }
}