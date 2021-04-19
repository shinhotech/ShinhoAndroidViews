package com.shinho.android.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.shinho.android.views.utils.DisplayUtils;
import com.shinho.android.views.R;


public class ProgressButton extends AppCompatTextView {

    public ProgressButton(Context context) {
        super(context);
        initView();
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.bg_f_button);
        setGravity(Gravity.CENTER);
        setTextColor(getResources().getColorStateList(R.color.txt_white2alpha_sel));
        int padding = DisplayUtils.dp2px(getContext(), 32);
        setPadding(padding,0,padding,0);
    }

    public void setGreenButton() {
        setBackgroundResource(R.drawable.bg_f_button_greeen);
        int padding = DisplayUtils.dp2px(getContext(), 32);
        setPadding(padding,0,padding,0);
    }


}
