package xyz.imaginarycrisis.wanandroidapp;

import android.annotation.SuppressLint;
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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;


public class IndexFragment extends Fragment {


    /**
     * 数据、adapter
     */
    private int bottomPage;
    private List<ArticleData> articleDataList;
    private IndexRvAdapter adapter;
    /**
     * 控件
     */
    private RecyclerView recyclerView;
    private View view0;
    private boolean firstStart = true;


    //=========自动生成============

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public IndexFragment() {
        // Required empty public constructor
    }

    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //=============================


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(firstStart) {
            bottomPage = 1;
            articleDataList = new ArrayList<>();
            adapter = new IndexRvAdapter(articleDataList, getContext());
        }
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view0 == null)
            view0 = inflater.inflate(R.layout.fragment_index, container, false);
        View view = view0;
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(firstStart) {
            recyclerView = getView().findViewById(R.id.index_rv);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firstStart) {
            setupScrollListener();
            requestData(bottomPage);
        }
        firstStart = false;
    }

    /**
     * 设置recyclerView滑动到底部刷新刷新的功能
     */
    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;

                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                }

                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    requestData(++bottomPage);
                }
            }
        });
    }

    /**
     * 网络请求相关
     * mHandler、requestData方法
     */

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //处理子线程获得的数据
            String tResponseData = (String)msg.obj;
            List<ArticleData> tDataList = ArticleData.getIndexArticlesDataFromJson(tResponseData);
            articleDataList.addAll(tDataList);
            adapter.notifyDataSetChanged();
        }
    };

    private void requestData(int pg){
        Runnable requestRunnable = () -> {
            try{
                URL url = new URL("https://www.wanandroid.com/article/list/"+(pg-1)+"/json");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setReadTimeout(8000);
                conn.setConnectTimeout(8000);
                conn.connect();
                InputStream in = conn.getInputStream();
                String responseData = Tools.streamToString(in);
                Message msg = new Message();
                msg.obj = responseData;
                mHandler.sendMessage(msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(requestRunnable).start();
    }


}