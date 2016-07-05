package com.dtlim.bantaystocks.common.customview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dale on 5/11/16.
 */

// Snackbar FAB behavior reference:
// https://lab.getbase.com/introduction-to-coordinator-layout-on-android/

public class ScrollFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private boolean isMovingSnackbar = false;

    public ScrollFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        isMovingSnackbar = true;
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
        isMovingSnackbar = false;
        child.setTranslationY(0);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if(dyConsumed > 0 && !isMovingSnackbar) {
            slideDown(child);
        }
        else if (dyConsumed < 0 && !isMovingSnackbar) {
            slideUp(child);
        }
    }

    private void slideDown(FloatingActionButton child) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int bottomMargin = params.bottomMargin;
        child.animate().translationY(child.getHeight() + bottomMargin).start();
    }

    private void slideUp(FloatingActionButton child) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int bottomMargin = params.bottomMargin;
        child.animate().translationY(0).start();
    }
}
