package com.dtlim.bantaystocks;

import android.app.Application;
import android.app.Service;
import android.content.Intent;

import com.dtlim.bantaystocks.data.database.DatabaseHelper;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.database.repository.SqliteDatabaseRepository;
import com.dtlim.bantaystocks.data.service.StocksService;
import com.facebook.stetho.Stetho;

/**
 * Created by dale on 6/23/16.
 */
public class BantayStocksApplication extends Application {

    private static DatabaseRepository sDatabaseRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
        startHomescreenStocksService();
    }

    public void initialize() {
        Stetho.initializeWithDefaults(this);
        sDatabaseRepository = new SqliteDatabaseRepository(
                new DatabaseHelper(this, "bantaystocks", 1));
    }

    public void startHomescreenStocksService() {
        Intent intent = new Intent(this, StocksService.class);
        startService(intent);
    }

    public static DatabaseRepository getDatabaseRepository() {
        return sDatabaseRepository;
    }
}
