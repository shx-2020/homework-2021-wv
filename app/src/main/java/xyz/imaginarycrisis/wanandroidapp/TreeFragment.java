package xyz.imaginarycrisis.wanandroidapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;




public class TreeFragment extends Fragment {

    /**
     * 数据
     */
    private int cid=1,currentPage=1;
    private List<ArticleData> dataList;

    /**
     * 回调
     */
    @SuppressLint("HandlerLeak")
    private Handler treeHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler articleHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tree, container, false);
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
}