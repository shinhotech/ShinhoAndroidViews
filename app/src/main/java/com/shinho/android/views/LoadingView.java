package com.shinho.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shinho.android.utils.DisplayUtils;
import com.shinho.android.utils.SimpleAnimListener;

/**
 * Created by chunyangli on 2018/3/13.
 */
public class LoadingView extends RelativeLayout {

    public static final float PRESS_Y = 0.1f;
    public static final float JUMP_Y = 0.3f;

    public static final long ANIM1_DUR = 100;
    public static final long ANIM2_DUR = 300;
    public static final long ANIM3_DUR = 300;

    private ImageView iv;

    private int[] images = new int[]{
            R.drawable.loading_1,
            R.drawable.loading_2,
            R.drawable.loading_3,
            R.drawable.loading_4};
    private int imageIndex = 0;
    private boolean isLoading;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setClipChildren(false);
        setClipToPadding(false);

        iv = new ImageView(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        int margin = DisplayUtils.dp2px(context, 16);
        params.setMargins(margin, margin, margin, margin);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(iv);

        loopImage();
    }

    /**
     * 显示静态图片
     */
    public void showStaticImage() {
        Animation animation = iv.getAnimation();
        if (animation != null) animation.cancel();
        iv.setImageResource(images[0]);
    }

    /**
     * 开始loading
     */
    public void startLoading() {
        startLoading(0, 0);
    }

    /**
     * 开始loading
     *
     * @param imageIndex 从第几个图片开始
     * @param animIndex  从动画组的第几个开始，一共3组动画
     */
    public void startLoading(int imageIndex, int animIndex) {
        isLoading = true;

        if (this.imageIndex != imageIndex) {
            this.imageIndex = imageIndex;
            loopImage();
        }

        switch (animIndex) {
            case 1:
                runAnim2();
                break;
            case 2:
                runAnim3();
                break;
            default:
                runAnim1();
                break;
        }
    }

    public void stopLoading() {
        isLoading = false;
        iv.clearAnimation();
        imageIndex = 0;
        loopImage();
    }

    // 动画组1 - 下压 同时 alpha显示
    private void runAnim1() {
        if (!isLoading) return;

        AnimationSet anim1 = new AnimationSet(true);
        anim1.addAnimation(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -PRESS_Y,
                Animation.RELATIVE_TO_SELF, 0f));
        anim1.addAnimation(new AlphaAnimation(0f, 1f));
        anim1.setDuration(ANIM1_DUR);
        anim1.setFillAfter(true);
        anim1.setAnimationListener(new SimpleAnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                runAnim2();
            }
        });
        iv.startAnimation(anim1);
    }

    // 动画组2 - 上移跳起
    private void runAnim2() {
        if (!isLoading) return;

        TranslateAnimation anim2 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -JUMP_Y);
        anim2.setDuration(ANIM2_DUR);
        anim2.setFillAfter(true);
        anim2.setAnimationListener(new SimpleAnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                runAnim3();
            }
        });
        iv.startAnimation(anim2);
    }

    // 动画组3 - 下落 同时 alpha消失
    private void runAnim3() {
        if (!isLoading) return;

        AnimationSet anim3 = new AnimationSet(true);
        anim3.addAnimation(new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -JUMP_Y,
                Animation.RELATIVE_TO_SELF, 0f));
        anim3.addAnimation(new AlphaAnimation(1f, 0f));
        anim3.setDuration(ANIM3_DUR);
        anim3.setFillAfter(true);
        anim3.setAnimationListener(new SimpleAnimListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                iv.postDelayed(() -> {
                    loopImage();
                    runAnim1();
                }, 100);
            }
        });
        iv.setAnimation(anim3);
    }

    private void loopImage() {
//        Log.i("DDD", "loopImage: " + imageIndex);
        iv.setImageResource(images[imageIndex]);
        imageIndex++;
        if (imageIndex == images.length) {
            imageIndex = 0;
        }
    }
}
