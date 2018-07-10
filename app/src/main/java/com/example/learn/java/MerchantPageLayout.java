package com.example.learn.java;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.learn.R;
import com.example.learn.business.MerchantCommentLayout;
import com.example.learn.business.MerchantFoodLayout;
import com.example.learn.business.MerchantInfoLayout;
import com.example.learn.view.SmartTabLayout1;
import com.example.learn.view.ViewPager2;

import org.jetbrains.annotations.NotNull;


/**
 * Created by Pisces on 2018/7/10 0010.
 */

public class MerchantPageLayout extends LinearLayout {

    private MerchantPageAdapter pagerAdapter;
    private Context mContext;
    SmartTabLayout1 vSmartTab;
    ViewPager2 vPager;

    public MerchantPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.merchant_page_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        vSmartTab = findViewById(R.id.vSmartTab);
        vPager = findViewById(R.id.vPager);
        this.pagerAdapter = new MerchantPageAdapter(mContext);
        vPager.setAdapter(pagerAdapter);
        vSmartTab.setViewPager(vPager);
    }

    public boolean canScrollVertically() {
        View view = ((ScrollableViewProvider) (pagerAdapter.getItem(vPager.getCurrentItem()))).getScrollableView();
        return view.canScrollVertically(-1);
    }
}

