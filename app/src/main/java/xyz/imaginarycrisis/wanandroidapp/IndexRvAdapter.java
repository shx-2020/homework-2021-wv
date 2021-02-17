package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.net.URL;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.core.content.ContextCompat.startActivity;

class IndexRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<IndexArticleData> dataList;
    private Context context;

    public static final int COMMON_ITEM = 1;
    public static final int HEAD_ITEM = 2;

    IndexRvAdapter(List<IndexArticleData> dataList,Context context){
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        if(COMMON_ITEM == viewType){
            View v = mInflater.inflate(R.layout.index_rv_item,parent,false);
            holder = new CommonViewHolder(v);
        }else{
            View v = mInflater.inflate(R.layout.index_head,parent,false);
            holder = new HeadViewHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CommonViewHolder){
            ((CommonViewHolder)holder).titleTv.setText(dataList.get(position).getTitle());
            ((CommonViewHolder)holder).authorOrShareUserTv.setText(dataList.get(position).getAuthorOrShareUser());
            ((CommonViewHolder)holder).infoTv.setText(dataList.get(position).getInfo());
        }else {
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEAD_ITEM;
        }else{
            return COMMON_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommonViewHolder extends RecyclerView.ViewHolder{
        TextView titleTv;
        TextView authorOrShareUserTv;
        TextView infoTv;
        public CommonViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.rv_title);
            authorOrShareUserTv = itemView.findViewById(R.id.rv_author);
            infoTv = itemView.findViewById(R.id.rv_info);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder{
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
}