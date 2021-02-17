package xyz.imaginarycrisis.wanandroidapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndexArticleData {
    public IndexArticleData(){return;}

    public IndexArticleData(String author,String shareUser,String title,String url,String niceShareDate,String niceDate,String chapterName,String superChapterName){
        this.author = author;this.shareUser = shareUser;this.title = title;
        this.url = url; this.niceShareDate = niceShareDate;this.niceDate = niceDate;
        this.chapterName = chapterName;this.superChapterName = superChapterName;
    }

    private String author,shareUser,title,url,niceDate,chapterName,superChapterName,niceShareDate;

    public String getAuthor() {
        return author;
    }

    public String getShareUser() {
        return shareUser;
    }

    public String getTitle() {
        String titleOut = title;
        titleOut = titleOut.replace("&lt;","<");
        titleOut = titleOut.replace("&gt;",">");
        titleOut = titleOut.replace("&middot;","·");
        return titleOut;
    }

    public String getUrl() {
        return url;
    }


    public String getNiceShareDate() {
        return niceShareDate;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getAuthorOrShareUser(){
        if(author.isEmpty()){
            return shareUser;
        }
        return author;
    }
    public String getTime(){
        String usableTime;
        if(niceDate.isEmpty())
            usableTime = niceShareDate;
        else
            usableTime = niceDate;
        return "时间："+usableTime;
    }

    public String getTag(){
        return "分类："+superChapterName+"/"+chapterName;
    }

    public static List<IndexArticleData> getIndexArticlesDataFromJson(String json){
        List<IndexArticleData> dataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray datas = data.getJSONArray("datas");
            for(int i=0;i<datas.length();i++){
                String author = datas.getJSONObject(i).getString("author");
                String shareUser = datas.getJSONObject(i).getString("shareUser");
                String niceShareDate = datas.getJSONObject(i).getString("niceShareDate");
                String niceDate = datas.getJSONObject(i).getString("niceDate");
                String title = datas.getJSONObject(i).getString("title");
                String url = datas.getJSONObject(i).getString("link");
                String chapterName = datas.getJSONObject(i).getString("chapterName");
                String superChapterName = datas.getJSONObject(i).getString("superChapterName");
                dataList.add(new IndexArticleData(author,shareUser,title,url,niceShareDate,niceDate,chapterName,superChapterName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("system","json decoding error");
        }
        return dataList;
    }
}
