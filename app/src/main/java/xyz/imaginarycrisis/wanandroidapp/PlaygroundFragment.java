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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;

public class PlaygroundFragment extends Fragment{
    private int currentPage = 1,currentPageCache = 1;
    private RecyclerView rv;
    private PlaygroundRvAdapter adapter;
    private List<ArticleData> dataList;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String tResponseData = (String)msg.obj;
            List<ArticleData> tDataList = ArticleData.getIndexArticlesDataFromJson(tResponseData);
            dataList.addAll(tDataList);
            adapter.notifyDataSetChanged();

        }
    };
    private boolean firstStart = true;
    private View view0;

    /**
     * ====================
     */
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public PlaygroundFragment() {
        // Required empty public constructor
    }
    public static PlaygroundFragment newInstance(String param1, String param2) {
        PlaygroundFragment fragment = new PlaygroundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *=======================
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view0 == null){
            view0 = inflater.inflate(R.layout.fragment_playground, container, false);}
        return view0;
    }
    private void init(){
        dataList = new ArrayList<>();
        rv = getView().findViewById(R.id.playground_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new PlaygroundRvAdapter(dataList,getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(firstStart) {
            init();
            requestData();
            setupScrollListener();
            firstStart = false;
        }
    }

    private void requestData(){
        new Thread(
                ()->
                {
                    try {
                        URL url = new URL("https://www.wanandroid.com/user_article/list/" + (currentPage - 1) + "/json");
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        connection.connect();
                        InputStream in = connection.getInputStream();
                        String responseData = Tools.streamToString(in);
                        Message msg = new Message();
                        msg.obj = responseData;
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        ).start();
    }

    private void setupScrollListener() {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    ++currentPage;
                    requestData();
                }
            }
        });
    }

}