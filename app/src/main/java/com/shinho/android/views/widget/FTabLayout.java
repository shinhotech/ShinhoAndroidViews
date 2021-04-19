package com.shinho.android.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.viewpager.widget.ViewPager;


import com.shinho.android.views.utils.DisplayUtils;
import com.shinho.android.views.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.Set;

/**
 * Created by chunyangli on 2018/2/5.
 */
public class FTabLayout extends MagicIndicator {

    private int textSizeSp;
    private int indicatorColor;
    private Set<Integer> hintList;
    private CommonNavigator commonNavigator;

    public FTabLayout(Context context) {
        super(context);
        init();
    }

    public FTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textSizeSp = 14;
        indicatorColor = getResources().getColor(R.color.txt_red);
    }

    public void setHintList(Set<Integer> hintList) {
        this.hintList = hintList;
        postInvalidate();
    }

    public void setTabTextSize(int textSizeSp) {
        this.textSizeSp = textSizeSp;
    }

    public void setTabTextColor(int indicatorColorRes) {
        this.indicatorColor = getResources().getColor(indicatorColorRes);
    }

    /**
     * 设置Viewpager
     */
    public void setupWithViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) return;

        commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return vp.getAdapter().getCount();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView tv = new ColorTransitionPagerTitleView(context);
                tv.setTextSize(textSizeSp);
                tv.setNormalColor(context.getResources().getColor(R.color.txt_black));
                tv.setSelectedColor(context.getResources().getColor(R.color.txt_red));
                tv.setText(vp.getAdapter().getPageTitle(index));
                tv.setOnClickListener(view -> vp.setCurrentItem(index));

                BadgePagerTitleView bptv = new BadgePagerTitleView(context);
                bptv.setInnerPagerTitleView(tv);
                bptv.setXBadgeRule(new BadgeRule(BadgeAnchor.RIGHT, DisplayUtils.dp2px(context, -14)));
                bptv.setYBadgeRule(new BadgeRule(BadgeAnchor.TOP, DisplayUtils.dp2px(context, 12)));
                if (hintList != null && hintList.contains(index)) {
                    bptv.setBadgeView(LayoutInflater.from(context).inflate(R.layout.view_red_point, null));
                } else {
                    bptv.setBadgeView(null);
                }
                return bptv;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(indicatorColor);
                indicator.setLineHeight(DisplayUtils.dp2px(context, 2));
                return indicator;
            }
        });
        setNavigator(commonNavigator);
        ViewPagerHelper.bind(this, vp);
    }

    public void notifyDataSetChanged() {
        commonNavigator.notifyDataSetChanged();
    }
}
