package com.dtlim.bantaystocks.data.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dtlim.bantaystocks.BantayStocksApplication;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;
import com.dtlim.bantaystocks.home.view.HomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 6/23/16.
 */
public class StocksNotificationService extends Service implements StocksNotificationRepository.ConnectionListener {

    private StocksNotificationRepository mStocksNotificationRepository = BantayStocksApplication.getStocksNotificationRepository();
    private DatabaseRepository mDatabaseRepository = BantayStocksApplication.getDatabaseRepository();

    public void onCreate() {
        super.onCreate();
        initialize();
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
        try {
            mStocksNotificationRepository.connect(this);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void saveStocksToDb(List<Stock> stocks) {
        mDatabaseRepository.insert(stocks);
    }

    @Override
    public void onConnectSuccess() {
        try {
            mStocksNotificationRepository.getStocks()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Stock>>() {
                        @Override
                        public void call(List<Stock> stocks) {
                            Log.d("MQTT", "MQTT call " + stocks.size() + " " + stocks.get(0).getName());
                            saveStocksToDb(stocks);
                        }
                    });
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectFail() {

    }
}
