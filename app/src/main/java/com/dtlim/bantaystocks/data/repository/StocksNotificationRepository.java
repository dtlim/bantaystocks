package com.dtlim.bantaystocks.data.repository;


import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

import rx.Observable;

/**
 * Created by dale on 6/28/16.
 */
public interface StocksNotificationRepository {
    void connect() throws Throwable;
    void subscribe(String... stocks) throws Throwable;
    void unsubscribe(String... stocks) throws Throwable;
    void unsubscribeAll() throws Throwable;
    Observable<List<Stock>> getStocks();
}
