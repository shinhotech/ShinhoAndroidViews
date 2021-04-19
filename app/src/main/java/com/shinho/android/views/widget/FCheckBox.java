package com.shinho.android.views.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.shinho.android.views.R;

/**
 * Created by chunyangli on 2018/2/9.
 */
public class FCheckBox extends AppCompatCheckBox {

    public FCheckBox(Context context) {
        super(context);
        initView(context);
    }

    public FCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        setBackgroundResource(R.drawable.cb_sel);
    }
}
