package com.dtlim.bantaystocks.data.repository.mock;

import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.StocksNotificationRepository;
import com.dtlim.bantaystocks.dummy.DummyModels;

import java.util.List;
import java.util.Random;

import rx.Observable;

/**
 * Created by dale on 6/29/16.
 */
public class FakeStocksNotificationRepository implements StocksNotificationRepository {
    final Random random = new Random();

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

    @Override
    public Observable<List<Stock>> getStocks() {
        final List<Stock> stocks = DummyModels.getDummyStockList();
//        return Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).timeInterval()
//            .flatMap(new Func1<TimeInterval<Long>, Observable<List<Stock>>>() {
//                @Override
//                public Observable<List<Stock>> call(TimeInterval<Long> longTimeInterval) {
//                    int i = random.nextInt(stocks.size());
//                    List<Stock> list = new ArrayList<>();
//                    Stock stock = stocks.get(i);
//                    stock.setPrice(new Price("PHP", random.nextInt(10000) + ".00"));
//                    stock.setPercentChange(random.nextInt(200) - 100 + ".00");
//                    list.add(stock);
//                    return Observable.just(list);
//                }
//            });
        return Observable.empty();
    }
}
