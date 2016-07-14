package com.dtlim.bantaystocks;

import com.dtlim.bantaystocks.data.repository.FakeStocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;

/**
 * Created by dale on 7/13/16.
 */
public class Injection {
    private Injection() {
    }

    public static StocksNotificationRepository provideStocksNotificationRepository() {
        return new FakeStocksNotificationRepository();
    }

    // TODO make fake
    public static DatabaseRepository provideDatabaseRepository(Context context) {
        return new SqliteDatabaseRepository(context);
    }

    public static SharedPreferencesRepository provideSharedPreferencesRepository(Context context) {
        return new LocalSharedPreferencesRepository(context);
    }
}
