package com.shinho.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.shinho.android.utils.DisplayUtils;

public class FRatingBar extends LinearLayout {

    public static final int MAX = 5;

    private int progress;

    private TextView textView;

    private static final String[] evaluateStrings = {"非常差", "差", "一般", "好", "非常好"};

    private boolean showEvaluateString = false;

    public FRatingBar(Context context) {
        super(context);
        initView(context, null);
    }

    public FRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);

        int size = DisplayUtils.dp2px(context, 40);
        for (int i = 0; i < MAX; i++) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(new LayoutParams(size, size));
            iv.setScaleType(ImageView.ScaleType.CENTER);
            iv.setImageResource(R.drawable.star_stroke);
            addView(iv);

            final int progress = i + 1;
            iv.setOnClickListener(v -> {
                setProgress(progress);
                if (listener != null) {
                    listener.onRatingChanged(null, progress, true);
                }
            });
        }
        textView = new TextView(getContext());
        textView.setTextSize(14);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.txt_oran));
        addView(textView);
        ((MarginLayoutParams) textView.getLayoutParams()).leftMargin = DisplayUtils.dp2px(getContext(), 8);
    }

    /**
     * 获取评分
     */
    public int getProgress() {
        return progress;
    }

    /**
     * 设置评分
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        for (int i = 0; i < MAX; i++) {
            ImageView iv = (ImageView) getChildAt(i);
            iv.setImageResource(progress > i ? R.drawable.star_solid : R.drawable.star_stroke);
        }
        if (showEvaluateString) {
            textView.setVisibility(VISIBLE);
            if (progress > 0 && progress <= 5) {
                textView.setText(evaluateStrings[progress - 1]);
            } else {
                textView.setText("");
            }
        }else {
            textView.setVisibility(GONE);
        }


    }


    /**
     * 是否显示文字评价
     */
    public void setShowEvaluateString(boolean showEvaluateString) {
        this.showEvaluateString = showEvaluateString;
        setProgress(progress);
    }

    private RatingBar.OnRatingBarChangeListener listener;

    /**
     * 设置评分变化监听
     */
    public void setOnRatingBarChangeListener(RatingBar.OnRatingBarChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 是否让评分居右，默认居左
     */
    public void setGravityLeft(boolean gravityLeft) {
        if (gravityLeft) {
            setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        } else {
            setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        }

    }

}
