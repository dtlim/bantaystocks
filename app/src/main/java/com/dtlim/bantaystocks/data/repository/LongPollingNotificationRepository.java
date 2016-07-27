package com.dtlim.bantaystocks.data.repository;

import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

import rx.Observable;

/**
 * Created by dale on 7/15/16.
 */
public class LongPollingNotificationRepository implements StocksNotificationRepository {
    @Override
    public void connect() throws Throwable {

    }

    @Override
    public void subscribe(String... stocks) throws Throwable {

    }

    @Override
    public void unsubscribe(String... stocks) {

    }

    @Override
    public void unsubscribeAll() {

    }

    // http://stackoverflow.com/questions/31768184/periodic-http-requests-using-rxjava-and-retrofit
    @Override
    public Observable<List<Stock>> getStocks() {
        return null;
    }
}
