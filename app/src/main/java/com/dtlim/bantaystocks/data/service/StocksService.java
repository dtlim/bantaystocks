package com.dtlim.bantaystocks.data.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.FakeStocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.MqttStocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;
import com.dtlim.bantaystocks.home.HomeActivity;
import com.dtlim.bantaystocks.home.customview.HomescreenItemTouchListener;
import com.dtlim.bantaystocks.home.customview.HomescreenStockItem;
import com.google.gson.Gson;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by dale on 6/23/16.
 */
public class StocksService extends Service {

    private WindowManager mWindowManager;
    private HomescreenStockItem mStockItem;
    private StocksNotificationRepository mStocksRepository = new FakeStocksNotificationRepository();

    private Gson gson = new Gson();
    Handler handler = new Handler();

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
        mStocksRepository.getStocks().subscribe(new Action1<List<Stock>>() {
            @Override
            public void call(List<Stock> stocks) {
                Log.d("MQTT", "MQTT call " + stocks.size() + " " + stocks.get(0).getName());
                updateHomeStocksView(stocks);
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
