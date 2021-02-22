package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

public class PrimaryRvAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<String> dataList;
    private Context context;
    private MyInnerActInterface treeInterface;
    private int PRIMARY_TAG_COMMON_VIEW_HOLDER_TYPE = 0;

    PrimaryRvAdapter(List<String>dataList, Context context){
        this.dataList = dataList;
        this.context = context;
        this.treeInterface = (MyInnerActInterface)context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder = null;
        if(PRIMARY_TAG_COMMON_VIEW_HOLDER_TYPE == viewType) {
            View v = mInflater.inflate(R.layout.tag_layout, parent, false);
            holder = new PrimaryViewHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof PrimaryViewHolder){
            String data = dataList.get(position);
            ((PrimaryViewHolder)holder).tagTextTv.setText(data);
            ((PrimaryViewHolder) holder).wrappingLinearLayout.setOnClickListener(v->treeInterface.changePrimaryTag(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class PrimaryViewHolder extends ViewHolder{
        private TextView tagTextTv;
        private LinearLayout wrappingLinearLayout;

        public PrimaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tagTextTv = itemView.findViewById(R.id.tag_text);
            wrappingLinearLayout = itemView.findViewById(R.id.tag_wrapping_linear_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return PRIMARY_TAG_COMMON_VIEW_HOLDER_TYPE;
    }
}
