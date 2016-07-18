package com.dtlim.bantaystocks.data.repository.mock;

import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.dummy.DummyModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * Created by dale on 7/17/16.
 */
public class FakeDatabaseRepository implements DatabaseRepository {

    List<Stock> list = DummyModels.getDummyStockList();
    BehaviorSubject<List<Stock>> mStocksSubject = BehaviorSubject.create();

    public FakeDatabaseRepository() {
        mStocksSubject.onNext(list);
    }

    @Override
    public long insert(Stock stock) {
        // simulate on conflict replace behavior
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getSymbol().equals(stock.getSymbol())) {
                list.remove(i);
                list.add(stock);
                return 1;
            }
        }
        list.add(stock);
        mStocksSubject.onNext(list);
        return 1;
    }

    @Override
    public long insert(List<Stock> stocks) {
        for(int i=0; i<stocks.size(); i++) {
            insert(stocks.get(i));
        }
        mStocksSubject.onNext(list);
        return stocks.size();
    }

    @Override
    public long update(Stock stock) {
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getSymbol().equals(stock.getSymbol())) {
                list.remove(i);
                list.add(stock);
                mStocksSubject.onNext(list);
                return 1;
            }
        }
        list.add(stock);
        mStocksSubject.onNext(list);
        return 0;
    }

    @Override
    public long delete(Stock stock) {
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getSymbol().equals(stock.getSymbol())) {
                list.remove(i);
                return 1;
            }
        }
        mStocksSubject.onNext(list);
        return 0;
    }

    @Override
    public Observable<List<Stock>> queryStocks() {
        return mStocksSubject;
    }

    @Override
    public Observable<List<Stock>> queryStocks(String... stockSymbols) {
        final List<String> queryStocks = Arrays.asList(stockSymbols);
        return mStocksSubject.concatMap(new Func1<List<Stock>, Observable<List<Stock>>>() {
            @Override
            public Observable<List<Stock>> call(List<Stock> stocks) {
                List<Stock> filteredList = new ArrayList<Stock>();
                for(int i=0; i<stocks.size(); i++) {
                    if(queryStocks.contains(stocks.get(i).getSymbol())) {
                        filteredList.add(stocks.get(i));
                    }
                }
                return Observable.just(filteredList);
            }
        });
    }
}
