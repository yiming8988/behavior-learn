package com.example.learn.java;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.learn.business.MerchantCommentLayout;
import com.example.learn.business.MerchantFoodLayout;
import com.example.learn.business.MerchantInfoLayout;

import org.jetbrains.annotations.NotNull;

public final class MerchantPageAdapter extends PagerAdapter {
    private final MerchantFoodLayout layFood;
    private final MerchantInfoLayout layInfo;
    private final MerchantCommentLayout layComment;

    public int getCount() {
        return 3;
    }


    @NotNull
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "点菜";
            case 1:
                return "评论(9999+)";
            case 2:
                return "商家";
            default:
                return "";
        }
    }

    @NotNull
    public Object instantiateItem(ViewGroup container, int position) {
        View content = this.getItem(position);
        container.addView(content);
        return content;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public final View getItem(int position) {
        switch (position) {
            case 0:
                return this.layFood;
            case 1:
                return this.layComment;
            case 2:
                return this.layInfo;
        }
        return null;
    }

    public MerchantPageAdapter(Context context) {
        super();
        this.layFood = new MerchantFoodLayout(context);
        this.layInfo = new MerchantInfoLayout(context);
        this.layComment = new MerchantCommentLayout(context);
    }
}
