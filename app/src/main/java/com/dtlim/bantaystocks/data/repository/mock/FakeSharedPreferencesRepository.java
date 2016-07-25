package com.dtlim.bantaystocks.data.repository.mock;

import android.util.Log;

import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dale on 7/15/16.
 */
public class FakeSharedPreferencesRepository implements SharedPreferencesRepository {

    private List<Listener> mListeners = new ArrayList<>();
    private String mWatchedStocks = "2GO,TEL";
    private String mSubscribedStocks = "2GO,TEL,MER";

    @Override
    public void saveSubscribedStocks(String[] stocks) {
        mSubscribedStocks = ParseUtility.encodeStockList(stocks);
    }

    @Override
    public void saveWatchedStocks(String[] stocks) {
        mWatchedStocks = ParseUtility.encodeStockList(stocks);
        broadcastPreferenceChange();
    }

    @Override
    public void registerSharedPreferencesListener(Listener listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterSharedPreferencesListener(Listener listener) {
        mListeners.remove(listener);
    }

    @Override
    public String getSubscribedStocks() {
        return mSubscribedStocks;
    }

    @Override
    public String getWatchedStocks() {
        return mWatchedStocks;
    }

    private void broadcastPreferenceChange() {
        for(int i=0; i<mListeners.size(); i++) {
            mListeners.get(i).onPreferenceChanged();
        }
    }
}
