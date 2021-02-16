package xyz.imaginarycrisis.wanandroidapp;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends PagerAdapter {

    List<BannerPageData> dataList= new ArrayList<>();

    @Override
    public int getCount() {
        return dataList == null ? 0 : Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position = position % dataList.size();
        BannerPageData item = dataList.get(position);
        return super.instantiateItem(container, position);
    }
}
