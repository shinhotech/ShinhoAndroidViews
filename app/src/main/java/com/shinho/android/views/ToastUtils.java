package com.shinho.android.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shinho.android.utils.DisplayUtils;

import java.lang.ref.WeakReference;

public class ToastUtils {

    private static WeakReference<Toast> reference;

    public enum Type {
        COMMON,
        SUCCESS,
        ERROR
    }

    /**
     * 非阻塞试显示Toast,防止出现连续点击Toast时的显示问题
     *
     * @param type ToastUtils.TYPE_SUCCESS, ToastUtils.TYPE_COMMON, ToastUtils.TYPE_WARNING
     */
    public static void showToast(Context context, CharSequence text, Type type) {
        try {
            if(reference == null || reference.get() == null) {
                createToast(context);
            }

            Toast toast = reference.get();
            TextView tv = toast.getView().findViewWithTag("tv");
            tv.setText(text);
            ImageView iv = toast.getView().findViewWithTag("iv");
            if (type == Type.SUCCESS) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_toast_success);
            } else if (type == Type.ERROR) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_toast_warning);
            } else {
                iv.setVisibility(View.GONE);
            }
            //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移0个单位，
            int yOffset = DisplayUtils.dp2px(context, -16);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, yOffset);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            // do nothing
        }
    }

    private static void createToast(Context context) {
        LinearLayout ll = new LinearLayout(context);
        ll.setBackgroundResource(R.drawable.bg_white_16shadow);
        ll.setGravity(Gravity.CENTER);
        ll.setLayoutParams(new ViewGroup.LayoutParams(
                DisplayUtils.getScreenWidth(context),
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setMinimumHeight(DisplayUtils.dp2px(context, 82));
        int padding = DisplayUtils.dp2px(context, 24);
        ll.setPadding(padding, padding, padding, padding);

        ImageView iv = new ImageView(context);
        iv.setTag("iv");
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ivParams.rightMargin = DisplayUtils.dp2px(context, 8);
        iv.setLayoutParams(ivParams);
        iv.setVisibility(View.GONE);
        ll.addView(iv);

        TextView tv = new TextView(context);
        tv.setContentDescription("toast_tv");
        tv.setTag("tv");
        tv.setTextSize(16);
        tv.setTextColor(context.getResources().getColor(R.color.txt_black));
        ll.addView(tv);

        FrameLayout view = new FrameLayout(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        view.addView(ll);

        Toast toast = new Toast(context);
        toast.setView(view);
        reference = new WeakReference<>(toast);
    }

    /**
     * 展示普通Toast
     */
    public static void showToast(Context context, CharSequence text) {
        showToast(context, text, Type.COMMON);
    }

}
