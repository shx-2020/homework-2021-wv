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
    private String responseData;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            responseData = (String)msg.obj;
            doAfterRequestDone();
        }
    };
    private EditText page_et;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaygroundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaygroundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaygroundFragment newInstance(String param1, String param2) {
        PlaygroundFragment fragment = new PlaygroundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        return inflater.inflate(R.layout.fragment_playground, container, false);
    }
    private void init(){
        dataList = new ArrayList<>();
        rv = getView().findViewById(R.id.playground_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new PlaygroundRvAdapter(dataList,getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        page_et = getView().findViewById(R.id.playground_et);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        requestData();
        setupListener();
    }

    private void requestData(){
        new Thread(
                ()->
                {
                    try {
                        URL url = new URL("https://www.wanandroid.com/user_article/list/"+(currentPage-1)+"/json");
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

    private void doAfterRequestDone(){
        if(ArticleData.getErrorCode(responseData)==0) {
            if(ArticleData.getIndexArticlesDataFromJson(responseData).isEmpty()){
                Toast.makeText(getContext(),"错误！\n该页无法找到任何文章", LENGTH_SHORT).show();
                currentPage = currentPageCache;
                return;
            }
            dataList.clear();
            dataList.addAll(ArticleData.getIndexArticlesDataFromJson(responseData));
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "跳转完成\n第" + currentPage + "页", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(),"错误！\n错误代码："+ ArticleData.getErrorCode(responseData)+
                    "\n错误信息："+ ArticleData.getErrorMsg(responseData),Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNumber(char c){
        boolean cIsNumberChar = false;
        for(int i=0;i<9;i++){
            if(Integer.valueOf(c)==i){
                cIsNumberChar = true;
                break;
            }
        }
        return cIsNumberChar;
    }

    private boolean containsNonNumber(String s){
        for(int i=0;i<s.length();i++){
            if (!isNumber(s.charAt(i)))
                return true;
        }
        return false;
    }

    private void setupListener(){
        page_et.setImeOptions(EditorInfo.IME_ACTION_GO);
        page_et.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_GO || (event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                String etContent = page_et.getText().toString();
                etContent.replace('\n','\0');
                if (!containsNonNumber(etContent))
                    Toast.makeText(getContext(),"请输入数字", LENGTH_SHORT).show();
                else {
                    if(Integer.parseInt(etContent) ==0)
                        Toast.makeText(getContext(),"第0页不存在。", LENGTH_SHORT).show();
                    else{
                        currentPageCache = currentPage;
                        currentPage = Integer.parseInt(etContent);
                        requestData();
                        return true;
                    }
                }

            }

            return false;
        });
    }
}