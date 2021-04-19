package com.shinho.android.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shinho.android.views.utils.DisplayUtils;
import com.shinho.android.views.R;

import java.util.Arrays;
import java.util.List;

public class FRadioGroup extends RecyclerView {

    public enum Orientation {
        GRID, // 网格
        VERTICAL, // 纵向
        HORIZONTAL // 横向
    }

    private RadioButtonAdapter adapter;
    private OnCheckedChangeListener onCheckedChangeListener;
    private int select = 0;
    private Orientation orientation;
    private boolean isDisabled;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public FRadioGroup(@NonNull Context context) {
        super(context);
    }

    public FRadioGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FRadioGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置选项，默认网格样式
     *
     * @param items 选项列表
     */
    public void setItems(String... items) {
        setItems(Orientation.GRID, items);
    }

    /**
     * 设置选项
     *
     * @param orientation 方向
     * @param items       选项列表
     */
    public void setItems(Orientation orientation, String... items) {
        if (items == null || items.length == 0) return;
        setItems(orientation, Arrays.asList(items));
    }

    /**
     * 设置选项
     *
     * @param orientation 方向
     * @param items       选项列表
     */
    public void setItems(Orientation orientation, List<String> items) {
        if (items == null || items.size() == 0) return;

        this.orientation = orientation;

        RecyclerView.LayoutManager manager;
        if (orientation == Orientation.VERTICAL) {
            manager = new LinearLayoutManager(getContext());
        } else if (orientation == Orientation.HORIZONTAL) {
            manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
            addItemDecoration(new HorizontalDecoration());
        } else {
            manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        }
        setLayoutManager(manager);
        adapter = new RadioButtonAdapter(items);
        setAdapter(adapter);
    }

    /**
     * 设置可用性
     *
     * @param isDisabled true-不可用
     */
    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取选中位置
     */
    public int getCheckPosition() {
        return select;
    }

    /**
     * 设置选中位置
     *
     * @param position 位置
     */
    public void setCheckPosition(int position) {
        this.select = position;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            toggleChange();
        }
    }

    private void toggleChange() {
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, select);
        }
    }

    private class RadioButtonAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> datas;

        public RadioButtonAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout ll = new LinearLayout(parent.getContext());
            int width = orientation == Orientation.HORIZONTAL
                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                    : ViewGroup.LayoutParams.MATCH_PARENT;
            ll.setLayoutParams(new ViewGroup.LayoutParams(width, DisplayUtils.dp2px(parent.getContext(), 32)));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setGravity(Gravity.CENTER_VERTICAL);
            return new ViewHolder(ll);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String data = datas.get(position);
            if (isDisabled) {
                holder.ivCheck.setImageResource(position == select ? R.drawable.rb_checked_disabled : R.drawable.rb_unchecked_disabled);
            } else {
                holder.ivCheck.setImageResource(position == select ? R.drawable.rb_checked : R.drawable.rb_unchecked);
            }
            holder.tvContent.setText(data);
            holder.itemView.setOnClickListener(view -> {
                select = position;
                notifyDataSetChanged();
                toggleChange();
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCheck;
        private TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);

            LinearLayout ll = (LinearLayout) itemView;
            ivCheck = new ImageView(itemView.getContext());
            ivCheck.setLayoutParams(new LinearLayout.LayoutParams(
                    DisplayUtils.dp2px(itemView.getContext(), 20),
                    DisplayUtils.dp2px(itemView.getContext(), 20)));
            ll.addView(ivCheck);

            tvContent = new TextView(itemView.getContext());
            tvContent.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvContent.setTextSize(16);
            tvContent.setTextColor(itemView.getResources().getColor(R.color.txt_black));
            tvContent.setPadding(DisplayUtils.dp2px(itemView.getContext(), 16), 0, 0, 0);
            ll.addView(tvContent);
        }
    }

    public static interface OnCheckedChangeListener {
        public void onCheckedChanged(ViewGroup group, int position);
    }
}
