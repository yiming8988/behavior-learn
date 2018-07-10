package com.example.learn.view;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;


/**
 * Created by Pisces on 2018/7/10 0010.
 */

public class ViewState {

    private int topMargin;
    private int bottomMargin;
    private int leftMargin;
    private int rightMargin;
    private int width;
    private int height;
    private float translationX;
    private float translationY;
    private float scaleX;
    private float scaleY;
    private float rotation;
    private float alpha;

    public static final void stateSet(View view, int tag, ViewState vs) {
        view.setTag(tag, vs);
    }

    public static final ViewState stateRead(View view, int tag) {
        Object vs = view.getTag(tag);
        if (!(vs instanceof ViewState)) {
            vs = null;
        }

        return (ViewState) vs;
    }

    public static final ViewState stateSave(View view, int tag) {
        ViewState viewState = stateRead(view, tag);
        if (viewState == null) {
            viewState = new ViewState();
        }

        ViewState vs = viewState;
        vs.copy(view);
        stateSet(view, tag, vs);
        return vs;
    }

    public static final void stateRefresh(View view, int tag1, int tag2, float p) {
        if (view instanceof AnimationUpdateListener) {
            ((AnimationUpdateListener) view).onAnimationUpdate(tag1, tag2, p);
        } else {
            ViewState vs1 = stateRead(view, tag1);
            ViewState vs2 = stateRead(view, tag2);
            if (vs1 != null && vs2 != null) {
                if (vs1.getTranslationX() != vs2.getTranslationX()) {
                    view.setTranslationX(vs1.getTranslationX() + (vs2.getTranslationX() - vs1.getTranslationX()) * p);
                }

                if (vs1.getTranslationY() != vs2.getTranslationY()) {
                    view.setTranslationY(vs1.getTranslationY() + (vs2.getTranslationY() - vs1.getTranslationY()) * p);
                }

                if (vs1.getScaleX() != vs2.getScaleX()) {
                    view.setScaleX(vs1.getScaleX() + (vs2.getScaleX() - vs1.getScaleX()) * p);
                }

                if (vs1.getScaleY() != vs2.getScaleY()) {
                    view.setScaleY(vs1.getScaleY() + (vs2.getScaleY() - vs1.getScaleY()) * p);
                }

                if (vs1.getRotation() != vs2.getRotation()) {
                    view.setRotation((vs1.getRotation() + (vs2.getRotation() - vs1.getRotation()) * p) % (float) 360);
                }

                if (vs1.getAlpha() != vs2.getAlpha()) {
                    view.setAlpha(vs1.getAlpha() + (vs2.getAlpha() - vs1.getAlpha()) * p);
                }

                ViewGroup.LayoutParams o = view.getLayoutParams();
                boolean lpChanged = false;
                if (vs1.getWidth() != vs2.getWidth()) {
                    o.width = (int) ((float) vs1.getWidth() + (float) (vs2.getWidth() - vs1.getWidth()) * p);
                    lpChanged = true;
                }

                if (vs1.getHeight() != vs2.getHeight()) {
                    o.height = (int) ((float) vs1.getHeight() + (float) (vs2.getHeight() - vs1.getHeight()) * p);
                    lpChanged = true;
                }

                ViewGroup.LayoutParams var10000 = o;
                if (!(o instanceof ViewGroup.MarginLayoutParams)) {
                    if (lpChanged) {
                        view.setLayoutParams(o);
                    }
                }

                if ((o instanceof ViewGroup.MarginLayoutParams)) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) o;
                    if (vs1.getTopMargin() != vs2.getTopMargin()) {
                        marginLayoutParams.topMargin = (int) ((float) vs1.getTopMargin() + (float) (vs2.getTopMargin() - vs1.getTopMargin()) * p);
                        lpChanged = true;
                    }

                    if (vs1.getBottomMargin() != vs2.getBottomMargin()) {
                        marginLayoutParams.bottomMargin = (int) ((float) vs1.getBottomMargin() + (float) (vs2.getBottomMargin() - vs1.getBottomMargin()) * p);
                        lpChanged = true;
                    }

                    if (vs1.getLeftMargin() != vs2.getLeftMargin()) {
                        marginLayoutParams.leftMargin = (int) ((float) vs1.getLeftMargin() + (float) (vs2.getLeftMargin() - vs1.getLeftMargin()) * p);
                        lpChanged = true;
                    }

                    if (vs1.getTopMargin() != vs2.getTopMargin()) {
                        marginLayoutParams.topMargin = (int) ((float) vs1.getTopMargin() + (float) (vs2.getTopMargin() - vs1.getTopMargin()) * p);
                        lpChanged = true;
                    }
                    if (lpChanged) {
                        view.setLayoutParams(marginLayoutParams);
                    }
                }


            }
        }

    }

    public static final ValueAnimator statesChangeByAnimation(final View[] views, final int tag1, final int tag2, float start, float end, final AnimationUpdateListener updateCallback, AnimatorListenerAdapter updateStateListener, long duration1, long startDelay1) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{start, end});
        valueAnimator.setStartDelay(startDelay1);
        valueAnimator.setDuration(duration1);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float p = (float) animation.getAnimatedValue();
                if (updateCallback != null) {
                    updateCallback.onAnimationUpdate(tag1, tag2, p);
                }
                for (View view : views) {
                    stateRefresh(view, tag1, tag2, p);
                }
            }
        });
        if (updateStateListener != null) {
            valueAnimator.addListener(updateStateListener);
        }
        valueAnimator.start();
        return valueAnimator;
    }

    public final int getTopMargin() {
        return this.topMargin;
    }

    public final void setTopMargin(int var1) {
        this.topMargin = var1;
    }

    public final int getBottomMargin() {
        return this.bottomMargin;
    }

    public final void setBottomMargin(int var1) {
        this.bottomMargin = var1;
    }

    public final int getLeftMargin() {
        return this.leftMargin;
    }

    public final void setLeftMargin(int var1) {
        this.leftMargin = var1;
    }

    public final int getRightMargin() {
        return this.rightMargin;
    }

    public final void setRightMargin(int var1) {
        this.rightMargin = var1;
    }

    public final int getWidth() {
        return this.width;
    }

    public final void setWidth(int var1) {
        this.width = var1;
    }

    public final int getHeight() {
        return this.height;
    }

    public final void setHeight(int var1) {
        this.height = var1;
    }

    public final float getTranslationX() {
        return this.translationX;
    }

    public final void setTranslationX(float var1) {
        this.translationX = var1;
    }

    public final float getTranslationY() {
        return this.translationY;
    }

    public final void setTranslationY(float var1) {
        this.translationY = var1;
    }

    public final float getScaleX() {
        return this.scaleX;
    }

    public final void setScaleX(float var1) {
        this.scaleX = var1;
    }

    public final float getScaleY() {
        return this.scaleY;
    }

    public final void setScaleY(float var1) {
        this.scaleY = var1;
    }

    public final float getRotation() {
        return this.rotation;
    }

    public final void setRotation(float var1) {
        this.rotation = var1;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float var1) {
        this.alpha = var1;
    }

    public final ViewState sx(float scaleX) {
        this.scaleX = scaleX;
        return this;
    }

    public final ViewState sxBy(float value) {
        this.scaleX *= value;
        return this;
    }

    public final ViewState sy(float scaleY) {
        this.scaleY = scaleY;
        return this;
    }

    public final ViewState syBy(float value) {
        this.scaleY *= value;
        return this;
    }

    public final ViewState a(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public final ViewState w(int width) {
        this.width = width;
        return this;
    }

    public final ViewState h(int height) {
        this.height = height;
        return this;
    }

    public final ViewState r(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public final ViewState ws(float s) {
        this.width = (int) ((float) this.width * s);
        return this;
    }

    public final ViewState hs(float s) {
        this.height = (int) ((float) this.height * s);
        return this;
    }

    public final ViewState tx(float translationX) {
        this.translationX = translationX;
        return this;
    }

    public final ViewState ty(float translationY) {
        this.translationY = translationY;
        return this;
    }

    public final ViewState ml(int leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    public final ViewState mr(int rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }

    public final ViewState mt(int topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    public final ViewState mn(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    public final ViewState copy(View view) {
        this.width = view.getWidth();
        this.height = view.getHeight();
        this.translationX = view.getTranslationX();
        this.translationY = view.getTranslationY();
        this.scaleX = view.getScaleX();
        this.scaleY = view.getScaleY();
        this.rotation = view.getRotation();
        this.alpha = view.getAlpha();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            layoutParams = null;
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            this.topMargin = marginLayoutParams.topMargin;
            this.bottomMargin = marginLayoutParams.bottomMargin;
            this.leftMargin = marginLayoutParams.leftMargin;
            this.rightMargin = marginLayoutParams.rightMargin;
        }
        return this;
    }

}

