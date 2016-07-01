package com.dtlim.bantaystocks.data.repository;


import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

import rx.Observable;

/**
 * Created by dale on 6/28/16.
 */
public interface StocksNotificationRepository {
    void subscribe(String... stocks);
    void unsubscribe(String... stocks);
    void unsubscribeAll();
    Observable<List<Stock>> getStocks();
}
