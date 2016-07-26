package com.dtlim.bantaystocks.select.presenter.impl;

import android.util.Log;

import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.select.presenter.SelectStocksPresenter;
import com.dtlim.bantaystocks.select.view.SelectStocksView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 7/4/16.
 */
public class SelectStocksPresenterImpl implements SelectStocksPresenter {
    private SelectStocksView mSelectView;
    private DatabaseRepository mDatabaseRepository;
    private SharedPreferencesRepository mSharedPreferencesRepository;

    public SelectStocksPresenterImpl(DatabaseRepository databaseRepository,
                             SharedPreferencesRepository sharedPreferencesRepository) {
        mDatabaseRepository = databaseRepository;
        mSharedPreferencesRepository = sharedPreferencesRepository;
    }

    @Override
    public void bindView(SelectStocksView view) {
        mSelectView = view;
    }

    @Override
    public void initializeDataFromDatabase() {
        Observable<List<Stock>> stocks = mDatabaseRepository.queryStocks();


        stocks.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Stock>>() {
                    @Override
                    public void call(List<Stock> stocks) {
                        Log.d("MOCKZ", "call: " + stocks.size());
                        mSelectView.setStocks(stocks);
                    }
                });
    }

    @Override
    public void initializeSubscribedStocks() {
        final String[] subscribedStocks = ParseUtility.parseStockList(
                mSharedPreferencesRepository.getSubscribedStocks());
        mSelectView.setSubscribedStocks(subscribedStocks);
    }

    @Override
    public void saveSubscribedStocks(String[] stockSymbols) {
        mSharedPreferencesRepository.saveSubscribedStocks(stockSymbols);
    }

    @Override
    public void onActivityResume() {

    }

    @Override
    public void onActivityPause() {

    }
}
