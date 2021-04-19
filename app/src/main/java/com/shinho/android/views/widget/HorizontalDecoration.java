package com.shinho.android.views.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shinho.android.views.utils.DisplayUtils;

public class HorizontalDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int left = DisplayUtils.dp2px(view.getContext(), position == 0 ? 0 : 24);
        outRect.set(left, 0, 0, 0);
    }

}
