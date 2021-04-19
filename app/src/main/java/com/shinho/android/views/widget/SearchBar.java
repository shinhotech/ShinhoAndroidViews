package com.shinho.android.views.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shinho.android.views.utils.DisplayUtils;
import com.shinho.android.views.utils.SimpleTextWatcher;
import com.shinho.android.views.utils.StringUtils;
import com.shinho.android.views.R;


/**
 * 搜索栏
 */
public class SearchBar extends FrameLayout {

    public static final int DEBOUNCE_SEARCH_TIME = 300;

    private ImageView iv_icon;
    private EditText et_search;
    private ImageView iv_clear;
    private TextView tv_cancel;

    private OnSearchListener listener;
//    private Disposable subscribe;
    private String searchKey = "";
    private Handler handler;

    Runnable runnable = () -> {
        if (listener == null || et_search.getText() == null) return;
        searchKey = et_search.getText().toString();
        if (StringUtils.isEmpty(searchKey)) {
            listener.onSearchRemove();
        } else {
            listener.onSearch(searchKey);
        }
    };
    public ImageView getIcon() {
        return iv_icon;
    }

    public EditText getEt() {
        return et_search;
    }

    public TextView getCancel() {
        return tv_cancel;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public SearchBar(Context context) {
        super(context);
        initView(null, 0);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {

        LayoutInflater.from(getContext()).inflate(R.layout.view_searchbar, this);
        handler = new Handler();
        iv_icon = findViewById(R.id.searchbar_iv_icon);
        et_search = findViewById(R.id.searchbar_et);
        iv_clear = findViewById(R.id.searchbar_iv_clear);
        tv_cancel = findViewById(R.id.searchbar_tv_cancel);
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.SearchBar, defStyleAttr, 0);
        if (!typedArray.getBoolean(R.styleable.SearchBar_showCancel, true)) {
            tv_cancel.setVisibility(GONE);
        }
        typedArray.recycle();
        if (isInEditMode()) return;
        // 延迟自动搜索
        setSearchMode(true);

        iv_clear.setOnClickListener(v -> et_search.setText(""));
        tv_cancel.setOnClickListener(v -> {
            Context context = getContext();
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });
    }

//    /**
//     * sfa:是否是延迟搜索模式
//     *
//     * @param isDebounceSearch true-输入结束后过一段时间后再搜索  false-立刻搜索
//     */
//    public void setSearchMode(boolean isDebounceSearch) {
//        if (subscribe != null) {
//            subscribe.dispose();
//        }
//
//        int time = isDebounceSearch ? DEBOUNCE_SEARCH_TIME : 0;
//        subscribe = Observable.create(e -> et_search.addTextChangedListener(new SimpleTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                iv_clear.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
//                e.onNext(s);
//            }
//        })).debounce(time, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(charSequence -> {
//                    if (listener == null) return;
//                    searchKey = charSequence.toString().trim();
//                    if (StringUtils.isEmpty(searchKey)) {
//                        listener.onSearchRemove();
//                    } else {
//                        listener.onSearch(searchKey);
//                    }
//                });
//    }


    /**
     * 是否是延迟搜索模式
     *
     * @param isDebounceSearch true-输入结束后过一段时间后再搜索  false-立刻搜索
     */
    public void setSearchMode(boolean isDebounceSearch) {
        handler.removeCallbacks(runnable);
        Log.d("test", "setSearchMode() called with: isDebounceSearch = [" + isDebounceSearch + "]");
        et_search.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iv_clear.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, isDebounceSearch ? DEBOUNCE_SEARCH_TIME : 0);
            }
        });

    }


    /**
     * 设置hint和搜索监听器
     */
    public void initSearchBar(String hint, OnSearchListener listener) {
        this.listener = listener;
        if (!TextUtils.isEmpty(hint)) {
            et_search.setHint(hint);
        }
    }

    /**
     * 保留软键盘
     */
    public void showSoftInput() {
        et_search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_search, 0);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    public interface OnSearchListener {
        void onSearchRemove();

        void onSearch(@NonNull String searchKey);
    }

    //解决点击区域有时候太小的问题
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(DisplayUtils.dp2px(getContext(), 28), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
