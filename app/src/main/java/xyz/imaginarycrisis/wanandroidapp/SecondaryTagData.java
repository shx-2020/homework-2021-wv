package xyz.imaginarycrisis.wanandroidapp;

import java.util.List;

public class SecondaryTagData {
    private List<ArticleData> articleDataList;
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<ArticleData> getArticleDataList() {
        return articleDataList;
    }
    SecondaryTagData(List<ArticleData> articleDataList,int id,String name){
        this.articleDataList = articleDataList;
        this.id = id;
        this.name = name;
    }
}
