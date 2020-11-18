package com.shinho.android.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;


/**
 * 底部栏
 */
public class BottomBar extends FrameLayout {

    private ImageView iv_back;
    private TextView tv_tip;
    private ProgressButton btn;
    private boolean shouldCheckAgain;
    private boolean alwaysOnlyRead;//如果为true，页面重试之后也不会显示提交按钮，否则会显示.

    public BottomBar(Context context) {
        super(context);
        initView(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public ImageView getIv() {
        return iv_back;
    }

    public TextView getTv() {
        return tv_tip;
    }

    public ProgressButton getBtn() {
        return btn;
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_bottombar, this);

        iv_back = findViewById(R.id.bottom_iv_back);
        tv_tip = findViewById(R.id.bottom_tv_tip);
        btn = findViewById(R.id.bottom_btn);

        iv_back.setOnClickListener(v -> {
            Context ctx = getContext();
//sfa:用于适配特定的 finish 逻辑
//            if(ctx instanceof BaseActivity) {
//                if (((BaseActivity)ctx).handleFinishActivity()) {
//                    return;
//                }
//            }

            if (ctx instanceof Activity) {
                ((Activity) ctx).finish();
            }
        });
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BottomBar);
        String text = a.getString(R.styleable.BottomBar_bottom_text);
        shouldCheckAgain = a.getBoolean(R.styleable.BottomBar_shouldCheckAgain, true);
        alwaysOnlyRead = a.getBoolean(R.styleable.BottomBar_alwaysOnlyRead, false);
        if (TextUtils.isEmpty(text)) {
            text = "提交";
        }
        btn.setText(text);
        setOnlyRead(a.getBoolean(R.styleable.BottomBar_onlyRead, false) || alwaysOnlyRead);
        a.recycle();
    }

    /**
     * 是否是永远只读状态
     */
    public boolean isAlwaysOnlyRead() {
        return alwaysOnlyRead;
    }

    // sfa:用于显示『重新』字样
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        updateBottomText();
//
//    }
//
//    public void setShouldCheckAgain(boolean shouldCheckAgain) {
//        this.shouldCheckAgain = shouldCheckAgain;
//        updateBottomText();
//    }
//
//    private void updateBottomText() {
//        boolean showAgain = shouldCheckAgain && getContext() instanceof BaseVisitWorkActivity &&
//                ((BaseVisitWorkActivity) getContext()).alreadyCommitted;
//        CharSequence btnText = btn.getText();
//        btn.setText(showAgain ? "重新" + btnText : btnText);
//    }

    /**
     * 设置只读状态
     */
    public void setOnlyRead(boolean onlyRead) {
        btn.setVisibility(onlyRead ? View.GONE : View.VISIBLE);
        tv_tip.setVisibility(onlyRead ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置永远只读状态
     */
    public void setAlwaysOnlyRead(boolean onlyRead) {
        alwaysOnlyRead = onlyRead;
        btn.setVisibility(onlyRead ? View.GONE : View.VISIBLE);
        tv_tip.setVisibility(onlyRead ? View.GONE : View.VISIBLE);
    }

    private long lastClickTime;

    /**
     * 设置点击时间
     */
    public void setup(OnClickListener listener) {
        if (listener == null) return;
        btn.setOnClickListener(v -> {
            // 间隔超过800毫秒才可以再次点击
            long diff = SystemClock.elapsedRealtime() - lastClickTime;
            if (diff < 800) {
                Log.i("DDD", "block bottom btn fast click");
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            listener.onClick(v);
        });
    }
}
