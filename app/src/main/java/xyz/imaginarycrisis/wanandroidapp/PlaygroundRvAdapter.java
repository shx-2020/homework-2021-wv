package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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

class PlaygroundRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ArticleData> dataList;
    private Context context;
    private MyOnClickListener myOnClickListener;
    public static final int COMMON_ITEM = 1;
    public static final int HEAD_ITEM = 2;

    PlaygroundRvAdapter(List<ArticleData> dataList, Context context){
        this.dataList = dataList;
        this.context = context;
        myOnClickListener = (MyOnClickListener)context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        if(COMMON_ITEM == viewType){
            View v = mInflater.inflate(R.layout.article_rv_item,parent,false);
            holder = new CommonViewHolder(v);
        }else{
            View v = mInflater.inflate(R.layout.share_article_item,parent,false);
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
            ((CommonViewHolder)holder).favBtn.setOnClickListener(v -> addFav());
        }else {
            ((HeadViewHolder)holder).articlePushTv.setOnClickListener(v->{
                pushArticle();
            });
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
        TextView articlePushTv;
        public HeadViewHolder(View itemView) {
            super(itemView);
            articlePushTv = itemView.findViewById(R.id.article_push_tv);
        }
    }

    private void pushArticle(){
        LayoutInflater factory = LayoutInflater.from(context);
        final View textEntryView = factory.inflate(R.layout.article_push_dialog, null);
        final EditText etLink = (EditText) textEntryView.findViewById(R.id.ap_dialog_et_link);
        final EditText etTitle = (EditText)textEntryView.findViewById(R.id.ap_dialog_et_title);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(context);
        ad1.setTitle("新建文章");
        ad1.setIcon(android.R.drawable.ic_dialog_alert);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("确定", (dialog, i) -> {
            myOnClickListener.requestArticlePush(etTitle.getText().toString(),etLink.getText().toString());
        });
        ad1.setNegativeButton("取消", (dialog, i) -> {
        });
        ad1.show();
    }
    public interface MyOnClickListener {
        public void requestArticlePush(String title,String link);
    }

}
