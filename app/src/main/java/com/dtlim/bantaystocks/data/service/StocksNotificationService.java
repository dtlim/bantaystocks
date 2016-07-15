package com.dtlim.bantaystocks.data.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dtlim.bantaystocks.BantayStocksApplication;
import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.FakeStocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.LocalSharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.MqttStocksNotificationRepository;
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
public class StocksNotificationService extends Service {

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
        Log.d("MQTT", "MQTT start notif service");
        try {
            mStocksNotificationRepository.connect();
            mStocksNotificationRepository.unsubscribeAll();
            mStocksNotificationRepository.subscribe("dale/stocks/ALLSTOCKS");
        }
        catch (Throwable t) {
            t.printStackTrace();
            EventBus.getDefault().post(t);
        }
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
        //initializeForeground();
    }

    private void initializeForeground() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("BantayStocks")
                .setContentInfo("BantayStocks is currently watching stock prices.")
                .setContentIntent(pendingIntent).build();

        startForeground(9998, notification);
    }

    private void saveStocksToDb(List<Stock> stocks) {
        mDatabaseRepository.insert(stocks);
    }
}
