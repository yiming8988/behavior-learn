package com.example.learn.java;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import com.example.learn.CandyKt;
import com.example.learn.R;
import com.example.learn.business.MerchantContentLayout;
import com.example.learn.java.MerchantPageLayout;
import com.example.learn.business.MerchantSettleLayout;
import com.example.learn.business.MerchantTitleLayout;
import com.example.learn.view.ViewPager2;


/**
 * Created by Pisces on 2018/7/10 0010.
 */

public class MerchantPageBehavior extends CoordinatorLayout.Behavior<MerchantPageLayout> {
    private MerchantPageLayout selfView;
    private MerchantTitleLayout layTitle;// 商店标题
    private ViewPager2 vPager;// 商品菜单所在pager
    private MerchantContentLayout layContent;// 商店详情
    private MerchantSettleLayout laySettle;
    private final int pagingTouchSlop;
    private int horizontalPagingTouch = 0; // 菜单横项列表(推荐商品)内容的触摸滑动距离
    private boolean isScrollRecommends = false;
    private int verticalPagingTouch = 0; // 菜单竖项列表(商品，评价，商家)内容的触摸滑动距离
    private int simpleTopDistance = 0;
    private boolean isScrollToFullFood = false; // 上滑显示商品菜单
    private boolean isScrollToHideFood = false; // 下滑显示商店详情
    private Scroller scroller;
    private final int scrollDuration;
    private Handler handler;
    private Runnable flingRunnable;
    private MerchantContentLayout.AnimatorListenerAdapter1 mAnimListener;

    public MerchantPageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pagingTouchSlop = CandyKt.dp(this, 5);
        this.scroller = new Scroller(context);
        this.scrollDuration = 800;
        this.handler = new Handler();
        this.scroller = new Scroller(context);
        this.flingRunnable = new Runnable() {
            public void run() {
                if (scroller.computeScrollOffset()) {
                    selfView.setTranslationY(scroller.getCurrY());
                    layContent.effectByOffset(scroller.getCurrY());
                    laySettle.effectByOffset(scroller.getCurrY());
                    handler.post(this);
                } else {
                    isScrollToHideFood = false;
                }

            }
        };
        this.mAnimListener = new MerchantContentLayout.AnimatorListenerAdapter1() {
            public void onAnimationStart(Animator animation, boolean toExpanded) {
                if (toExpanded) {
                    int defaultDisplayHeight = selfView.getHeight() - simpleTopDistance;

                    scroller.startScroll(0, (int) selfView.getTranslationY(), 0, (int) (defaultDisplayHeight - selfView.getTranslationY()), scrollDuration);
                } else {
                    scroller.startScroll(0, (int) selfView.getTranslationY(), 0, (int) (-selfView.getTranslationY()), scrollDuration);
                }
                handler.post(flingRunnable);
                isScrollToHideFood = true;
            }
        };
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, MerchantPageLayout child, int layoutDirection) {
        selfView = child;
        vPager = child.findViewById(R.id.vPager);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) selfView.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            simpleTopDistance = lp.topMargin - layTitle.getHeight();
            lp.height = parent.getHeight() - layTitle.getHeight();
            child.setLayoutParams(lp);
            return true;
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MerchantPageLayout child, View dependency) {
        if (dependency.getId() == R.id.layTitle) {
            this.layTitle = (MerchantTitleLayout) dependency;
        } else if (dependency.getId() == R.id.layContent) {
            MerchantContentLayout merchantContentLayout = (MerchantContentLayout) dependency;
            merchantContentLayout.setAnimListener(this.mAnimListener);
            this.layContent = merchantContentLayout;
        } else if (dependency.getId() == R.id.laySettle) {
            this.laySettle = (MerchantSettleLayout) dependency;
        } else {
            return false;
        }
        return true;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MerchantPageLayout child, View dependency) {
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, MerchantPageLayout child, View directTargetChild, View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, MerchantPageLayout child, View directTargetChild, View target, int axes, int type) {
        scroller.abortAnimation();
        isScrollToHideFood = false;
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, MerchantPageLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (isScrollToHideFood) {
            consumed[1] = dy;
            return; // scroller 滑动中.. do nothing
        }

        verticalPagingTouch += dy;
        if (vPager.isScrollable() && Math.abs(verticalPagingTouch) > pagingTouchSlop) {
            vPager.setScrollable(false); // 屏蔽 pager横向滑动干扰
        }

        horizontalPagingTouch += dx;
        if (R.id.vRecommends == target.getId()) {
            if (!isScrollRecommends) {
                consumed[0] = dx;
                if (vPager.isScrollable() && Math.abs(horizontalPagingTouch) > pagingTouchSlop)
                    isScrollRecommends = true;
            }
            if (isScrollRecommends) {
                consumed[1] = dy;
                return; // 横项滑动推荐列表中
            }
        }

        if ((child.getTranslationY() < 0 || (child.getTranslationY() == 0F && dy > 0)) && !child.canScrollVertically()) {
            Float effect = layTitle.effectByOffset(dy);
            selfView.findViewById(R.id.vSmartTab).setBackgroundColor((int) CandyKt.getArgbEvaluator().evaluate(effect, Color.WHITE, 0xFFFAFAFA));
            float transY = -simpleTopDistance * effect;
            if (transY != child.getTranslationY()) {
                child.setTranslationY(transY);
                consumed[1] = dy;
            }

            if (type == 1) {
                isScrollToFullFood = true;
            }
        } else if ((child.getTranslationY() > 0 || (child.getTranslationY() == 0F && dy < 0)) && !child.canScrollVertically()) {
            if (isScrollToFullFood) {
                child.setTranslationY(0F);  // top fling to bottom
            } else {
                child.setTranslationY(child.getTranslationY() - (float) dy);
                layContent.effectByOffset(child.getTranslationY());
                laySettle.effectByOffset(child.getTranslationY());
            }
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull MerchantPageLayout child, @NonNull View target, float velocityX, float velocityY) {
        return onUserStopDragging();
    }


    private final boolean onUserStopDragging() {

        if (selfView.getTranslationY() < 0.0F) {
            return false;
        } else {

            int defaultDisplayHeight = selfView.getHeight() - this.simpleTopDistance;

            if ((float) defaultDisplayHeight * 0.4F > selfView.getTranslationY()) {
                scroller.startScroll(0, (int) selfView.getTranslationY(), 0, (int) (-selfView.getTranslationY()), this.scrollDuration);
                layContent.switchExpanded(false, true);
                laySettle.switchExpanded(false);
            } else {
                scroller.startScroll(0, (int) selfView.getTranslationY(), 0, (int) (defaultDisplayHeight - selfView.getTranslationY()), scrollDuration);
                layContent.switchExpanded(true, true);
                laySettle.switchExpanded(true);
            }

            this.handler.post(this.flingRunnable);
            this.isScrollToHideFood = true;
            return true;
        }
    }

}
