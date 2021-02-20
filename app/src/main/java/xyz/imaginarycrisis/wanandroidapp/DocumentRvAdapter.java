package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DocumentRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DocumentArticleData> dataList;
    private Context context;

    public static final int COMMON_ITEM = 1;

    DocumentRvAdapter(List<DocumentArticleData> dataList, Context context){
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        if(COMMON_ITEM == viewType){
            View view = mInflater.inflate(R.layout.article_rv_item,parent,false);
            holder = new CommonViewHolder(view);
        }
        return holder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DocumentRvAdapter.CommonViewHolder){
            ((DocumentRvAdapter.CommonViewHolder)holder).titleTv.setText(dataList.get(position).getTitle());
            ((DocumentRvAdapter.CommonViewHolder)holder).authorOrShareUserTv.setText(dataList.get(position).getAuthor());
            ((DocumentRvAdapter.CommonViewHolder)holder).tagTv.setText(dataList.get(position).getChapterName());
            ((DocumentRvAdapter.CommonViewHolder)holder).timeTv.setText(dataList.get(position).getNiceDate());
            ((DocumentRvAdapter.CommonViewHolder)holder).layout.setOnClickListener(v -> {
                WebViewActivity.actStart(context,dataList.get(position).getLink(),dataList.get(position).getTitle());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return COMMON_ITEM;
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
}
