package com.dtlim.bantaystocks;

import android.content.Context;

import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.database.repository.SqliteDatabaseRepository;
import com.dtlim.bantaystocks.data.repository.LocalSharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.PeriodicRetrofitNotificationRepository;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;

/**
 * Created by dale on 7/13/16.
 */
public class Injection {
    private Injection() {
    }

    public static StocksNotificationRepository provideStocksNotificationRepository() {
        return new PeriodicRetrofitNotificationRepository();
    }

    public static DatabaseRepository provideDatabaseRepository(Context context) {
        return new SqliteDatabaseRepository(context);
    }

    public static SharedPreferencesRepository provideSharedPreferencesRepository(Context context) {
        return new LocalSharedPreferencesRepository(context);
    }
}
