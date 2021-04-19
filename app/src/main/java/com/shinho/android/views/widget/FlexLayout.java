package com.shinho.android.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.shinho.android.views.R;

import java.util.ArrayList;
import java.util.List;

public class FlexLayout extends RecyclerView {

    private List<String> tags = new ArrayList<>();
    private FlexTagAdapter adapter;

    public FlexLayout(Context context) {
        super(context);
        initView(context);
    }

    public FlexLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FlexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private FlexboxItemDecoration getItemDecoration() {
        FlexboxItemDecoration decoration = new FlexboxItemDecoration(getContext());
        decoration.setOrientation(FlexboxItemDecoration.BOTH);
        decoration.setDrawable(getResources().getDrawable(R.drawable.rect_flex_direction));
        return decoration;
    }

    /**
     * 设置标签
     *
     * @param tags 标签列表
     */
    public void setItems(List<String> tags) {
        if (tags == null) return;
        removeAllViews();

        this.tags.clear();
        this.tags.addAll(tags);
        adapter.notifyDataSetChanged();
    }

    private void initView(Context context) {
        setLayoutManager(new FlexboxLayoutManager(context, FlexDirection.ROW));
        FlexboxItemDecoration decoration = getItemDecoration();
        if (decoration != null) {
            addItemDecoration(decoration);
        }

        adapter = new FlexTagAdapter();
        setAdapter(adapter);
    }

    private class FlexTagAdapter extends RecyclerView.Adapter<FlexLayout.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_gray_solid, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tag.setText(tags.get(position));
            holder.tag.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(null, holder.tag, position, 0);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tags.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tag;

        public ViewHolder(View itemView) {
            super(itemView);

            tag = (TextView) itemView;
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
