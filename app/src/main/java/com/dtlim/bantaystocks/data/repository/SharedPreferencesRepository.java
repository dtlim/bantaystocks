package com.dtlim.bantaystocks.data.repository;

/**
 * Created by dale on 7/1/16.
 */
public interface SharedPreferencesRepository {
    void saveSubscribedStocks(String stocks);
    void saveWatchedStocks(String stocks);
    String getSubscribedStocks();
    String getWatchedStocks();
}
