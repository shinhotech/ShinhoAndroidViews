package com.shinho.android.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

/**
 * Created by chunyangli on 2018/2/1.
 */
public class FBottomDialog extends Dialog {

    public FBottomDialog(@NonNull Context context) {
        super(context, R.style.FDialog);

        //设置窗口出现和窗口隐藏的动画
        getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    @Override
    public void show() {
        // Dialog的位置置底
        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }

        super.show();
    }

}
