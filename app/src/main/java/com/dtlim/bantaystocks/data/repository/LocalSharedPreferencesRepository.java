package com.dtlim.bantaystocks.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dtlim.bantaystocks.common.utility.ParseUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dale on 7/1/16.
 */
public class LocalSharedPreferencesRepository implements SharedPreferencesRepository,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_SUBSCRIBED_STOCKS = "Key Subscribed Stocks";
    public static final String KEY_WATCHED_STOCKS = "Key Watched Stocks";

    private SharedPreferences mSharedPreferences;

    private List<Listener> mListeners = new ArrayList<>();

    public LocalSharedPreferencesRepository(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void saveSubscribedStocks(String[] stocks) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stockList = ParseUtility.encodeStockList(stocks);
        editor.putString(KEY_SUBSCRIBED_STOCKS, stockList);
        editor.apply();
    }

    @Override
    public void saveWatchedStocks(String[] stocks) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stockList = ParseUtility.encodeStockList(stocks);
        editor.putString(KEY_WATCHED_STOCKS, stockList);
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

    @Override
    public void registerSharedPreferencesListener(Listener listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterSharedPreferencesListener(Listener listener) {
        mListeners.remove(listener);
    }

    private void broadcastPreferenceChange() {
        for(int i=0; i<mListeners.size(); i++) {
            mListeners.get(i).onPreferenceChanged();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(KEY_WATCHED_STOCKS)) {
            broadcastPreferenceChange();
        }
    }
}
