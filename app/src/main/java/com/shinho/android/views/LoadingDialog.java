package com.shinho.android.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * Created by chunyangli on 2018/3/13.
 */
public class LoadingDialog extends Dialog {

    private LoadingView loading;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.FDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_loading_view);
        loading = findViewById(R.id.loading);
    }

    @Override
    public void show() {
        super.show();
        loading.startLoading();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        loading.stopLoading();
    }
}
