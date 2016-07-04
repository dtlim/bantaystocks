package com.dtlim.bantaystocks.data.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.dtlim.bantaystocks.BantayStocksApplication;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.database.table.StockTable;
import com.dtlim.bantaystocks.data.model.Price;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.home.view.HomeActivity;
import com.dtlim.bantaystocks.home.customview.HomescreenItemTouchListener;
import com.dtlim.bantaystocks.home.customview.HomescreenStockItem;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 6/23/16.
 */
public class StocksDisplayService extends Service {

    private WindowManager mWindowManager;
    private HomescreenStockItem mStockItem;
    private DatabaseRepository mDatabaseRepository = BantayStocksApplication.getDatabaseRepository();

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mStockItem != null) {
            mWindowManager.removeView(mStockItem);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initialize() {
        Log.d("MQTT", "MQTT start display service");
        Observable<List<Stock>> stocksObservable = mDatabaseRepository.queryStocks();

        stocksObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Stock>>() {
                    @Override
                    public void call(List<Stock> stocks) {
                        if(stocks != null && !stocks.isEmpty()) {
                            updateHomeStocksView(stocks);
                        }
                    }
                });

        initializeForeground();
        addHomescreenStockView();
    }

    private void initializeForeground() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("BantayStocks")
                .setContentText("BantayStocks is currently running")
                .setContentIntent(pendingIntent).build();

        startForeground(9999, notification);
    }

    private void addHomescreenStockView() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mStockItem = new HomescreenStockItem(this);
        final WindowManager.LayoutParams params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        mStockItem.setOnTouchListener(new HomescreenItemTouchListener(mWindowManager, params));
        mWindowManager.addView(mStockItem, params);
    }

    private void updateHomeStocksView(List<Stock> stocks) {
        mStockItem.setStock(stocks.get(0));
    }
}
