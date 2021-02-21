package xyz.imaginarycrisis.wanandroidapp;

import java.util.List;

public class PrimaryTagData {
    private List<SecondaryTagData> secondaryTagDataList;
    private int id;
    private String name;

    PrimaryTagData(List<SecondaryTagData> secondaryTagDataList,int id,String name){
        this.secondaryTagDataList = secondaryTagDataList;
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<SecondaryTagData> getSecondaryTagDataList() {
        return secondaryTagDataList;
    }

    public String getName() {
        return name;
    }
}
