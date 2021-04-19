package com.shinho.android.views.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.shinho.android.views.utils.DisplayUtils;
import com.shinho.android.views.R;

import java.util.List;

/**
 * ios样式 底部选择对话框
 */
public class BottomSelectDialog extends FBottomDialog {

    public List<Integer> icons;
    public List<String> strs;
    public AdapterView.OnItemClickListener listener;

    /**
     * 初始化并且设置图标文字和点击监听器
     */
    public BottomSelectDialog(@NonNull Context context, List<Integer> icons, List<String> strs, AdapterView.OnItemClickListener
            listener) {
        super(context);

        this.icons = icons;
        this.strs = strs;
        this.listener = listener;

        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_bottom_select);

        findViewById(R.id.bottom_select_cancel).setOnClickListener(v -> dismiss());

        LinearLayout container = findViewById(R.id.bottom_select_ll);
        for (int i = 0; i < strs.size(); i++) {
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    DisplayUtils.dp2px(getContext(), 68)));
            ll.setGravity(Gravity.CENTER_VERTICAL);

            if (icons != null && icons.size() > i && icons.get(i) != null) {
                Integer icon = icons.get(i);
                ImageView iv = new ImageView(getContext());
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(
                        DisplayUtils.dp2px(getContext(), 32),
                        DisplayUtils.dp2px(getContext(), 32));
                ivParams.setMargins(DisplayUtils.dp2px(getContext(), 24), 0, 0, 0);
                iv.setLayoutParams(ivParams);
                iv.setImageResource(icon);
                iv.setScaleType(ImageView.ScaleType.CENTER);
                ll.addView(iv);
            }

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setPadding(DisplayUtils.dp2px(getContext(), 24), 0, 0, 0);
            tv.setText(strs.get(i));
            tv.setTextSize(16);
            tv.setTextColor(getContext().getResources().getColor(R.color.txt_black));
            ll.addView(tv);

            final int position = i;
            ll.setOnClickListener(v -> {
                dismiss();
                if (listener != null) listener.onItemClick(null, v, position, -1);
            });

            if (position > 0) {
                View divider = new View(getContext());
                divider.setBackgroundResource(R.color.divider_gray);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                dividerParams.setMargins(
                        DisplayUtils.dp2px(getContext(), 24), 0,
                        DisplayUtils.dp2px(getContext(), 24), 0);
                divider.setLayoutParams(dividerParams);
                container.addView(divider);
            }

            container.addView(ll);
        }

    }
}
