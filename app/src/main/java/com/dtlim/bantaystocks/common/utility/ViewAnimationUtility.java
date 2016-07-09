package com.dtlim.bantaystocks.common.utility;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by dale on 7/9/16.
 */
public class ViewAnimationUtility {

    public static AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    public static DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    public static void playScaleUpAnimation(View view, int duration, int startDelay) {
        view.setScaleX(0);
        view.setScaleY(0);

        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(ACCELERATE_INTERPOLATOR)
                .start();
    }

    public static void playScaleDownAnimation(View view, int duration, int startDelay) {
        view.setScaleX(1);
        view.setScaleY(1);

        view.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setInterpolator(ACCELERATE_INTERPOLATOR)
                .start();
    }
}
