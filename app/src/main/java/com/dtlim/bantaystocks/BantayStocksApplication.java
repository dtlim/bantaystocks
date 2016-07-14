package com.dtlim.bantaystocks;

import android.app.Application;
import android.content.Intent;

import com.dtlim.bantaystocks.data.database.DatabaseHelper;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.database.repository.SqliteDatabaseRepository;
import com.dtlim.bantaystocks.data.repository.LocalSharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.MqttStocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;
import com.dtlim.bantaystocks.data.service.StocksDisplayService;
import com.dtlim.bantaystocks.data.service.StocksNotificationService;
import com.facebook.stetho.Stetho;

/**
 * Created by dale on 6/23/16.
 */
public class BantayStocksApplication extends Application {

    private static DatabaseRepository sDatabaseRepository;
    private static SharedPreferencesRepository sSharedPreferencesRepository;
    private static StocksNotificationRepository sStocksNotificationRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
        startStocksNotificationService();
        startStocksDisplayService();
    }

    public void initialize() {
        Stetho.initializeWithDefaults(this);
        sDatabaseRepository = Injection.provideDatabaseRepository(this);
        sSharedPreferencesRepository = Injection.provideSharedPreferencesRepository(this);
        sStocksNotificationRepository = Injection.provideStocksNotificationRepository();
        sSharedPreferencesRepository.saveWatchedStocks(new String[] {});
    }

    public void startStocksDisplayService() {
        Intent intent = new Intent(this, StocksDisplayService.class);
        startService(intent);
    }

    public void startStocksNotificationService() {
        Intent intent = new Intent(this, StocksNotificationService.class);
        startService(intent);
    }

    public static DatabaseRepository getDatabaseRepository() {
        return sDatabaseRepository;
    }

    public static SharedPreferencesRepository getSharedPreferencesRepository() {
        return sSharedPreferencesRepository;
    }

    public static StocksNotificationRepository getStocksNotificationRepository() {
        return sStocksNotificationRepository;
    }
}
