package com.dtlim.bantaystocks.home.customview;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by dale on 6/24/16.
 */
public class HomescreenItemTouchListener implements View.OnTouchListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private int dx = 0;
    private int dy = 0;

    public HomescreenItemTouchListener(WindowManager windowManager, WindowManager.LayoutParams params) {
        mWindowManager = windowManager;
        mParams = params;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int touchX = (int) event.getRawX();
        int touchY = (int) event.getRawY();

        Log.d("TOUCHEZ", "TOUCHEZ " + touchX + " " + touchY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = touchX - mParams.x;
                dy = touchY - mParams.y;
                break;
            case MotionEvent.ACTION_MOVE: {
                mParams.x = touchX - dx;
                mParams.y = touchY - dy;
                mWindowManager.updateViewLayout(view, mParams);
                break;
            }

            default:
                return false;
        }
        return true;
    }
}
