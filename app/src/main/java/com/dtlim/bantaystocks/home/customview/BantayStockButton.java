package com.dtlim.bantaystocks.home.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.common.utility.BitmapUtility;

/**
 * Created by dale on 7/5/16.
 */
public class BantayStockButton extends ImageView {

    private boolean isWatched = false;

    private int framerate = 10;
    int radiusIncrement = 2;
    int radius = 0;
    boolean isAnimating = false;
    boolean isExpanding = false;

    Drawable image;
    Bitmap originalBitmap;
    Bitmap drawableBitmap;
    Canvas bitmapCanvas;

//    Paint paint = new Paint();
//    Paint maskPaint = new Paint();

//    private final Handler handler = new Handler();
//    private final Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            invalidate();
//        }
//    };

    public BantayStockButton(Context context) {
        super(context);
        initialize();
    }

    public BantayStockButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public BantayStockButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        Log.d("DRAWZ", "DRAWZ starting to draw");
//        image = ContextCompat.getDrawable(getContext(), R.drawable.bantaystocks_icon_watch_stock_enabled);
        image = ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher);
        originalBitmap = BitmapUtility.drawableToBitmap(image);
        drawableBitmap = BitmapUtility.drawableToBitmap(image);
        //originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bantaystocks_icon_watch_stock_enabled);
        Log.d("DRAWZ", "DRAWZ starting to draw 1" + (originalBitmap == null));
        //drawableBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
        Log.d("DRAWZ", "DRAWZ starting to draw 2" + (drawableBitmap == null));
        bitmapCanvas = new Canvas(drawableBitmap);

//        maskPaint.setColor(Color.RED);
//        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//
//        radius = isWatched ? getWidth() : 0;
    }

    public void setIsWatched(boolean bool) {
//        isWatched = bool;
//        isExpanding = bool;
//        doOnClickAnimation();
//        invalidate();
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        if(isAnimating) {
//            drawableBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
//            if(isExpanding) {
//                doExpandingAnimation();
//            }
//            else {
//                doContractingAnimation();
//            }
//        }
//
//        bitmapCanvas.setBitmap(drawableBitmap);
//        bitmapCanvas.drawCircle(getWidth()/2, getHeight()/2, radius, maskPaint);
//        canvas.drawBitmap(drawableBitmap, 0, 0, paint);
//
//        handler.postDelayed(runnable, framerate);
//    }

    public void doOnClickAnimation() {
        if(!isAnimating) {
            isAnimating = true;
        }
    }

    private void doExpandingAnimation() {
        radius += radiusIncrement;
        if(radius >= getWidth()/2 && radius >= getHeight()/2) {
            isAnimating = false;
        }
    }

    private void doContractingAnimation() {
        radius -= radiusIncrement;
        if(radius <= 0) {
            isAnimating = false;
        }
    }
}
