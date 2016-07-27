package com.dtlim.bantaystocks.common.utility;

import android.animation.Animator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by dale on 7/9/16.
 */
public class ViewAnimationUtility {

    public static AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    public static DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    public static void playScaleUpAnimation(final View view, int duration, int startDelay) {
        view.setScaleX(0);
        view.setScaleY(0);
        view.setVisibility(View.VISIBLE);
        if(view.getParent() instanceof View) {
            ((View) view.getParent()).setVisibility(View.VISIBLE);
        }
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(ACCELERATE_INTERPOLATOR)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    public static void playScaleDownAnimation(final View view, int duration, int startDelay) {
        view.setScaleX(1);
        view.setScaleY(1);

        view.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(ACCELERATE_INTERPOLATOR)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                        if(view.getParent() instanceof View) {
                            ((View) view.getParent()).setVisibility(View.GONE);
                        }
                        Log.d("ANIMZ", "SET TO GONE");
                        view.clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }
}
