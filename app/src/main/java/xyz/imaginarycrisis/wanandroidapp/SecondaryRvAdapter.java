package xyz.imaginarycrisis.wanandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecondaryRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PrimaryTagData> dataList;
    private Context context;
    private MyTreeInterface treeInterface;
    private int PRIMARY_TAG_COMMON_VIEW_HOLDER_TYPE = 0;

    SecondaryRvAdapter(List<PrimaryTagData>dataList, Context context){
        this.dataList = dataList;
        this.context = context;
        this.treeInterface = (MyTreeInterface)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        if(PRIMARY_TAG_COMMON_VIEW_HOLDER_TYPE == viewType) {
            View v = mInflater.inflate(R.layout.tag_layout, parent, false);
            holder = new SecondaryViewHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SecondaryViewHolder){
            PrimaryTagData data = dataList.get(position);
            ((SecondaryViewHolder)holder).tagTextTv.setText(data.getName());
            ((SecondaryViewHolder)holder).wrappingLinearLayout.setOnClickListener(
                    v->treeInterface.getSecondaryChapterList(data.getId())
            );
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class SecondaryViewHolder extends RecyclerView.ViewHolder {
        private TextView tagTextTv;
        private LinearLayout wrappingLinearLayout;

        public SecondaryViewHolder(@NonNull View itemView) {
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
