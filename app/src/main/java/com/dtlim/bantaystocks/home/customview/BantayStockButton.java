package com.dtlim.bantaystocks.home.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dtlim.bantaystocks.common.utility.BitmapUtility;

/**
 * Created by dale on 7/5/16.
 */
public class BantayStockButton extends ImageView {

    int radius = 7;
    Drawable image;
    Bitmap drawableBitmap;
    Canvas bitmapCanvas;

    Paint paint = new Paint();
    Paint maskPaint = new Paint();

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
        image = getDrawable();
        drawableBitmap = BitmapUtility.drawableToBitmap(image);
        bitmapCanvas = new Canvas(drawableBitmap);

        maskPaint.setColor(Color.RED);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        bitmapCanvas.drawCircle(getWidth()/2, getHeight()/2, radius, maskPaint);
        canvas.drawBitmap(drawableBitmap, 0, 0, paint);
    }
}
