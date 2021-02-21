package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DocumentRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DocumentArticleData> dataList;
    private Context context;
    MyDocumentInterface myDocumentInterface;

    public static final int COMMON_ITEM = 1;

    DocumentRvAdapter(List<DocumentArticleData> dataList, Context context){
        this.dataList = dataList;
        this.context = context;
        myDocumentInterface = (MyDocumentInterface)context;
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
            ((CommonViewHolder)holder).titleTv.setText(dataList.get(position).getTitle());
            ((CommonViewHolder)holder).authorOrShareUserTv.setText(dataList.get(position).getAuthor());
            ((CommonViewHolder)holder).tagTv.setText(dataList.get(position).getChapterName());
            ((CommonViewHolder)holder).timeTv.setText(dataList.get(position).getNiceDate());
            ((CommonViewHolder)holder).layout.setOnClickListener(v -> {
                WebViewActivity.actStart(context,dataList.get(position).getLink(),dataList.get(position).getTitle());
            });
            ((CommonViewHolder)holder).favBtn.setOnClickListener(v->myDocumentInterface.deleteDocument(dataList.get(position).getId()));
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
