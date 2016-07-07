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
import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.database.table.StockTable;
import com.dtlim.bantaystocks.data.model.Price;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.home.view.HomeActivity;
import com.dtlim.bantaystocks.home.customview.HomescreenItemTouchListener;
import com.dtlim.bantaystocks.home.customview.HomescreenStockItem;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 6/23/16.
 */
public class StocksDisplayService extends Service implements SharedPreferencesRepository.Listener{

    private WindowManager mWindowManager;
    private DatabaseRepository mDatabaseRepository = BantayStocksApplication.getDatabaseRepository();
    private SharedPreferencesRepository mSharedPreferencesRepository =
            BantayStocksApplication.getSharedPreferencesRepository();

    private HashMap<String, HomescreenStockItem> mStockItems;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferencesRepository.unregisterSharedPreferencesListener(this);
        for(String key : mStockItems.keySet()) {
            mWindowManager.removeView(mStockItems.get(key));
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
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mStockItems = new HashMap<>();
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

        mSharedPreferencesRepository.registerSharedPreferencesListener(this);

        initializeForeground();
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

    private HomescreenStockItem createHomescreenStockView() {
        HomescreenStockItem stockItem = new HomescreenStockItem(this);
        final WindowManager.LayoutParams params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        stockItem.setOnTouchListener(new HomescreenItemTouchListener(mWindowManager, params));
        mWindowManager.addView(stockItem, params);
        return stockItem;
    }

    private void updateHomeStocksView(List<Stock> stocks) {
        for(int i=0; i<stocks.size(); i++) {
            Stock currentStock = stocks.get(i);
            if(mStockItems.get(currentStock.getSymbol()) != null) {
                HomescreenStockItem currentItem = mStockItems.get(currentStock.getSymbol());
                currentItem.setStock(currentStock);
            }
        }
    }

    @Override
    public void onPreferenceChanged() {
        List<String> watchedStocks = Arrays.asList(
                ParseUtility.parseStockList(mSharedPreferencesRepository.getWatchedStocks()));
        Collection<String> hashMapKeys = mStockItems.keySet();

        for(int i=0; i<watchedStocks.size(); i++) {
            String currentStock = watchedStocks.get(i);
            if(!hashMapKeys.contains(currentStock)) {
                HomescreenStockItem item = createHomescreenStockView();
                mStockItems.put(currentStock, item);
            }
        }
    }
}
