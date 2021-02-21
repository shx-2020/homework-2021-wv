package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TreeRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ArticleData> dataList;
    private Context context;
    private MyTreeInterface treeInterface;
    private int TREE_COMMON_VIEW_HOLDER_TYPE = 0;

    TreeRvAdapter(List<ArticleData>dataList, Context context){
        this.dataList = dataList;
        this.context = context;
        this.treeInterface = (MyTreeInterface)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        if(TREE_COMMON_VIEW_HOLDER_TYPE == viewType) {
            View v = mInflater.inflate(R.layout.article_rv_item, parent, false);
            holder = new PlaygroundRvAdapter.CommonViewHolder(v);

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  TreeViewHolder){
            ArticleData data = dataList.get(position);
            ((TreeViewHolder) holder).titleTv.setText(data.getTitle());
            ((TreeViewHolder) holder).authorTv.setText(data.getAuthorOrShareUser());
            ((TreeViewHolder) holder).timeTv.setText(data.getTime());
            ((TreeViewHolder) holder).tagTv.setText(data.getTag());
            ((TreeViewHolder) holder).wrappingLinearLayout.setOnClickListener(
                    v ->
                    WebViewActivity.actStart(context,data.getUrl(),data.getTitle())
            );
            ((TreeViewHolder) holder).favBtn.setOnClickListener(
                    v->treeInterface.addDocument(data.getId())
            );
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TreeViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTv;
        private TextView authorTv;
        private TextView timeTv;
        private TextView tagTv;
        private LinearLayout wrappingLinearLayout;
        private ImageButton favBtn;

        public TreeViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.rv_time);
            titleTv = itemView.findViewById(R.id.rv_title);
            authorTv = itemView.findViewById(R.id.rv_author);
            tagTv = itemView.findViewById(R.id.rv_tags);
            wrappingLinearLayout = itemView.findViewById(R.id.pop_lay);
            favBtn = itemView.findViewById(R.id.fav_btn);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TREE_COMMON_VIEW_HOLDER_TYPE;
    }
}
