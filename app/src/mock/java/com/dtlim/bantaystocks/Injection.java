package com.dtlim.bantaystocks;

import android.content.Context;

import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;
import com.dtlim.bantaystocks.data.repository.mock.FakeDatabaseRepository;
import com.dtlim.bantaystocks.data.repository.mock.FakeSharedPreferencesRepository;
import com.dtlim.bantaystocks.data.repository.mock.FakeStocksNotificationRepository;

/**
 * Created by dale on 7/13/16.
 */
public class Injection {
    private Injection() {
    }

    public static StocksNotificationRepository provideStocksNotificationRepository() {
        return new FakeStocksNotificationRepository();
    }

    public static DatabaseRepository provideDatabaseRepository(Context context) {
        return new FakeDatabaseRepository();
    }

    public static SharedPreferencesRepository provideSharedPreferencesRepository(Context context) {
        return new FakeSharedPreferencesRepository();
    }
}
