package com.dtlim.bantaystocks.home.presenter.impl;

import android.database.Cursor;

import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.database.Database;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.database.repository.SqliteDatabaseRepository;
import com.dtlim.bantaystocks.data.database.table.StockTable;
import com.dtlim.bantaystocks.data.model.Price;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.home.presenter.HomePresenter;
import com.dtlim.bantaystocks.home.view.HomeView;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 7/1/16.
 */
public class HomePresenterImpl implements HomePresenter {

    private HomeView mHomeView;
    private DatabaseRepository mDatabaseRepository;
    private SharedPreferencesRepository mSharedPreferencesRepository;

    public HomePresenterImpl(DatabaseRepository databaseRepository,
                             SharedPreferencesRepository sharedPreferencesRepository) {
        mDatabaseRepository = databaseRepository;
        mSharedPreferencesRepository = sharedPreferencesRepository;
    }

    @Override
    public void bindView(HomeView view) {
        mHomeView = view;
    }

    @Override
    public void initializeData() {
        getStocksFromSharedPreferences();
    }

    private void getStocksFromSharedPreferences() {
        String subscribedStocks = mSharedPreferencesRepository.getSubscribedStocks();
        String[] subscribedStocksList = ParseUtility.parseStockList(subscribedStocks);

        Observable<SqlBrite.Query> stocks = mDatabaseRepository.queryStocks(subscribedStocksList);

        stocks.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SqlBrite.Query>() {
                    @Override
                    public void call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        List<Stock> list = parseCursor(cursor);
                        if(list != null && !list.isEmpty()) {
                            mHomeView.setSubscribedStocks(list);
                        }
                    }
                });
    }

    // TODO delete this
    protected List<Stock> parseCursor(Cursor cursor) {
        List<Stock> stockList = new ArrayList<>();

        try{
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    Stock stock = new Stock();
                    stock.setName(cursor.getString(cursor.getColumnIndex(StockTable.NAME)));
                    stock.setPercentChange(cursor.getString(cursor.getColumnIndex(StockTable.PERCENT_CHANGE)));
                    stock.setVolume(cursor.getString(cursor.getColumnIndex(StockTable.VOLUME)));
                    stock.setSymbol(cursor.getString(cursor.getColumnIndex(StockTable.SYMBOL)));
                    Price price = new Price(cursor.getString(cursor.getColumnIndex(StockTable.CURRENCY)),
                            cursor.getString(cursor.getColumnIndex(StockTable.PRICE)));
                    stock.setPrice(price);
                    stockList.add(stock);
                }
                while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return stockList;
    }
}
