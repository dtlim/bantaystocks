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
import com.dtlim.bantaystocks.data.repository.MqttStocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;
import com.dtlim.bantaystocks.home.HomeActivity;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 6/23/16.
 */
public class StocksNotificationService extends Service {

    private StocksNotificationRepository mStocksRepository = new MqttStocksNotificationRepository();
    private DatabaseRepository mDatabaseRepository = BantayStocksApplication.getDatabaseRepository();

    @Override
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
        Log.d("MQTT", "MQTT start notif service");
        mStocksRepository.getStocks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Stock>>() {
                    @Override
                    public void call(List<Stock> stocks) {
                        Log.d("MQTT", "MQTT call " + stocks.size() + " " + stocks.get(0).getName());
                        saveStocksToDb(stocks);
                    }
                });
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

        startForeground(9998, notification);
    }

    private void saveStocksToDb(List<Stock> stocks) {
        for(int i=0; i<stocks.size(); i++) {
            if(mDatabaseRepository.update(stocks.get(i)) <= 0) {
                mDatabaseRepository.insert(stocks.get(i));
            }
        }
    }
}
