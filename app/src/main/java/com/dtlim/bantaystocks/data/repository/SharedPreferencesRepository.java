package com.dtlim.bantaystocks.data.repository;

/**
 * Created by dale on 7/1/16.
 */
public interface SharedPreferencesRepository {

    interface Listener {
        void onPreferenceChanged();
    }

    void saveSubscribedStocks(String[] stocks);
    void saveWatchedStocks(String[] stocks);
    void registerSharedPreferencesListener(Listener listener);
    void unregisterSharedPreferencesListener(Listener listener);
    String getSubscribedStocks();
    String getWatchedStocks();
}
