package com.dtlim.bantaystocks.data.database.repository;

import com.dtlim.bantaystocks.data.model.Stock;

import rx.Observable;

/**
 * Created by dale on 6/29/16.
 */
public interface DatabaseRepository {
    long insert(Stock stock);
    long update(Stock stock);
    long delete(Stock stock);
    Observable queryStocks();
}
