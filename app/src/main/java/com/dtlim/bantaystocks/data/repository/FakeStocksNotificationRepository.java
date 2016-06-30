package com.dtlim.bantaystocks.data.repository;

import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.dummy.DummyModels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.TimeInterval;

/**
 * Created by dale on 6/29/16.
 */
public class FakeStocksNotificationRepository implements StocksNotificationRepository {
    final Random random = new Random();

    @Override
    public void subscribe(String... stocks) {

    }

    @Override
    public void unsubscribe(String... stocks) {

    }

    @Override
    public Observable<List<Stock>> getStocks() {
        final List<Stock> stocks = DummyModels.getDummyStockList();
        return Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).timeInterval()
                .flatMap(new Func1<TimeInterval<Long>, Observable<List<Stock>>>() {
                    @Override
                    public Observable<List<Stock>> call(TimeInterval<Long> longTimeInterval) {
                        int i = random.nextInt(stocks.size());
                        List<Stock> list = new ArrayList<>();
                        list.add(stocks.get(i));
                        return Observable.just(list);
                    }
                });
    }
}
