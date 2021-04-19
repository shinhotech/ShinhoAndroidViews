package com.shinho.android.views.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.shinho.android.views.utils.DisplayUtils;
import com.shinho.android.views.R;


/**
 *这里是圆形进度条
 *方形的只需要用ProgressBar然后设置style="@style/FProgressBar"即可
 */
public class CircleView extends View {
    private Paint mPaint;
    private int mCircleBgColor;
    private int mCircleLightColor;
    private int mCircleDarkColor;
    private int mLineWidth;
    private Shader mShader;
    private float mAngle;
    private int mViewWidth;
    private int mViewHeight;
    private RectF mArcRectF;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mCircleBgColor = ContextCompat.getColor(getContext(), R.color.bg_gray);
        mLineWidth = DisplayUtils.dp2px(getContext(), 10);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 设置弧度，最大360
     */
    public void setAngle(float angle) {
        mAngle = angle;
        if (angle < 360) {
            mCircleLightColor = ContextCompat.getColor(getContext(), R.color.circle_red_light);
            mCircleDarkColor = ContextCompat.getColor(getContext(), R.color.circle_red_dark);
        }else {
            mCircleLightColor = ContextCompat.getColor(getContext(), R.color.circle_green_light);
            mCircleDarkColor = ContextCompat.getColor(getContext(), R.color.circle_green_dark);
        }
        setupShader();
        postInvalidate();
    }

    /**
     * 设置百分比
     */
    public void setPercent(float percent) {
        setAngle(percent * 360);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        int radius = Math.min(height, width) / 2 - mLineWidth/2;
        mPaint.setColor(mCircleBgColor);
        canvas.drawCircle(height / 2, width / 2, radius, mPaint);
        mPaint.setShader(mShader);
        canvas.drawArc(mArcRectF, -90, mAngle, false, mPaint);
        mPaint.setShader(null);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        setupShader();
    }

    private void setupShader() {
        mShader = new LinearGradient(mLineWidth/2, 0, mViewWidth - mLineWidth/2, 0, mCircleLightColor,
                mCircleDarkColor, Shader.TileMode.CLAMP);
        mArcRectF = new RectF(mLineWidth/2, mLineWidth/2, mViewWidth - mLineWidth/2, mViewHeight - mLineWidth/2);
    }
}
