package xyz.imaginarycrisis.wanandroidapp;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DocumentArticleData {
    private String title,chapterName,niceDate,link,author;
    private int id;

    public DocumentArticleData(String title,String author,String link,String niceDate,String chapterName,int id){
        this.title=title;this.chapterName=chapterName;this.niceDate=niceDate;this.link=link;this.author=author;this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getChapterName() {
        return "章节："+chapterName;
    }

    public String getNiceDate() {
        return "时间："+niceDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public static List<DocumentArticleData> getArticleDataFromJson(String json){
        List<DocumentArticleData> dataList = new ArrayList<>();
        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray datas = data.getJSONArray("datas");
            for(int i=0;i<datas.length();i++){
                String title = datas.getJSONObject(i).getString("title");
                String author = datas.getJSONObject(i).getString("author");
                String link = datas.getJSONObject(i).getString("link");
                String niceDate = datas.getJSONObject(i).getString("niceDate");
                String chapterName = datas.getJSONObject(i).getString("chapterName");
                int id = datas.getJSONObject(i).getInt("id");
                dataList.add(new DocumentArticleData(title,author,link,niceDate,chapterName,id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
