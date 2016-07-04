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

        Observable<List<Stock>> stocks = mDatabaseRepository.queryStocks(subscribedStocksList);

        stocks.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Stock>>() {
                    @Override
                    public void call(List<Stock> stocks) {
                        if(stocks != null && !stocks.isEmpty()) {
                            mHomeView.setSubscribedStocks(stocks);
                        }
                    }
                });
    }
}
