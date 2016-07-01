package com.dtlim.bantaystocks.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dale on 7/1/16.
 */
public class LocalSharedPreferencesRepository implements SharedPreferencesRepository {

    public static final String KEY_SUBSCRIBED_STOCKS = "Key Subscribed Stocks";
    public static final String KEY_WATCHED_STOCKS = "Key Watched Stocks";

    private SharedPreferences mSharedPreferences;

    public LocalSharedPreferencesRepository(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void saveSubscribedStocks(String stocks) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_SUBSCRIBED_STOCKS, stocks);
        editor.apply();
    }

    @Override
    public void saveWatchedStocks(String stocks) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_WATCHED_STOCKS, stocks);
        editor.apply();
    }

    @Override
    public String getSubscribedStocks() {
        return mSharedPreferences.getString(KEY_SUBSCRIBED_STOCKS, "");
    }

    @Override
    public String getWatchedStocks() {
        return mSharedPreferences.getString(KEY_WATCHED_STOCKS, "");
    }

}
