package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private void addFav(){
        Toast.makeText(context,"你点击了收藏按钮",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CommonViewHolder){
            ((CommonViewHolder)holder).titleTv.setText(dataList.get(position).getTitle());
            ((CommonViewHolder)holder).authorOrShareUserTv.setText(dataList.get(position).getAuthorOrShareUser());
            ((CommonViewHolder)holder).tagTv.setText(dataList.get(position).getTag());
            ((CommonViewHolder)holder).timeTv.setText(dataList.get(position).getTime());
            ((CommonViewHolder)holder).layout.setOnClickListener(v -> {
                WebViewActivity.actStart(context,dataList.get(position).getUrl(),dataList.get(position).getTitle());
            });
            ((CommonViewHolder)holder).favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFav();
                }
            });
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
        TextView tagTv;
        TextView timeTv;
        ImageButton favBtn;
        LinearLayout layout;
        public CommonViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.rv_title);
            authorOrShareUserTv = itemView.findViewById(R.id.rv_author);
            tagTv = itemView.findViewById(R.id.rv_tags);
            timeTv = itemView.findViewById(R.id.rv_time);
            favBtn = itemView.findViewById(R.id.fav_btn);
            layout = itemView.findViewById(R.id.pop_lay);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder{
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
}