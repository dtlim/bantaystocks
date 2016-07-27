package com.dtlim.bantaystocks.home.customview;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by dale on 6/24/16.
 */
public class HomescreenItemTouchListener implements View.OnTouchListener {

    private static int THRESHOLD = 10;

    private WindowManager mWindowManager;
    private ViewGroup mParent;
    private WindowManager.LayoutParams mParams;
    private int startX = 0;
    private int startY = 0;
    private int dx = 0;
    private int dy = 0;
    private int offsetX = 0;
    private int offsetY = 0;
    private boolean isDragging = false;

    public HomescreenItemTouchListener(WindowManager windowManager, ViewGroup parent,
                                       WindowManager.LayoutParams params) {
        mWindowManager = windowManager;
        mParent = parent;
        mParams = params;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int touchX = (int) event.getRawX();
        int touchY = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = touchX;
                startY = touchY;
                offsetX = startX - mParams.x;
                offsetY = startY - mParams.y;
                break;
            case MotionEvent.ACTION_UP:
                if(isDragging) {
                    isDragging = false;
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE: {
                dx = touchX - startX;
                dy = touchY - startY;
                if(Math.abs(dx) > THRESHOLD || Math.abs(dy) > THRESHOLD) {
                    mParams.x = touchX - offsetX;
                    mParams.y = touchY - offsetY;
                    mWindowManager.updateViewLayout(mParent, mParams);
                    isDragging = true;
                    return true;
                }
            }
        }
        return false;
    }
}
