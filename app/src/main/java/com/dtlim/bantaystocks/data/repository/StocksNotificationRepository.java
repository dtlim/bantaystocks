package com.dtlim.bantaystocks.data.repository;


import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

import rx.Observable;

/**
 * Created by dale on 6/28/16.
 */
public interface StocksNotificationRepository {

    interface ConnectionListener {
        void onConnectSuccess();
        void onConnectFail();
    }

    void connect(ConnectionListener listener) throws Throwable;
    Observable<List<Stock>> getStocks();
}
