package com.dtlim.bantaystocks.home.presenter.impl;

import android.util.Log;

import com.dtlim.bantaystocks.common.utility.ParseUtility;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.home.presenter.HomePresenter;
import com.dtlim.bantaystocks.home.view.HomeView;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dale on 7/1/16.
 */
public class HomePresenterImpl implements HomePresenter, SharedPreferencesRepository.Listener {

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
    public void onActivityResume() {
        initializeData();
        mSharedPreferencesRepository.registerSharedPreferencesListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityPause() {
        mSharedPreferencesRepository.unregisterSharedPreferencesListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initializeData() {
        setStocksFromSharedPreferences();
        setWatchedStocksFromSharedPreferences();
    }

    @Override
    public void watchStock(Stock stock) {
        String[] list = ParseUtility.parseStockList(mSharedPreferencesRepository.getWatchedStocks());
        ArrayList<String> stocks = new ArrayList<>(Arrays.asList(list));
        if(stocks.contains(stock.getSymbol())) {
            stocks.remove(stock.getSymbol());
        }
        else {
            stocks.add(stock.getSymbol());
        }
        mSharedPreferencesRepository.saveWatchedStocks(stocks.toArray(new String[stocks.size()]));
        setWatchedStocksFromSharedPreferences();
    }

    @Override
    public void removeStock(Stock stock) {
        String[] list = ParseUtility.parseStockList(mSharedPreferencesRepository.getSubscribedStocks());
        ArrayList<String> stocks = new ArrayList<>(Arrays.asList(list));
        stocks.remove(stock.getSymbol());
        mSharedPreferencesRepository.saveSubscribedStocks(stocks.toArray(new String[stocks.size()]));
        setStocksFromSharedPreferences();
    }

    @Subscribe
    public void onEvent(Throwable throwable) {
        Log.d("EVENTBUZ", "EVENTBUZ received from service");
        throwable.printStackTrace();
        if(throwable instanceof MqttException) {
            mHomeView.showMessage("An error has occurred.  Trying to connect again...");
        }
        else {
            mHomeView.showMessage("An error has occurred.");
        }
    }

    private void setStocksFromSharedPreferences() {
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
                            mHomeView.hideNoSubscribedStocks();
                        }
                        else {
                            mHomeView.setSubscribedStocks(Collections.EMPTY_LIST);
                            mHomeView.showNoSubscribedStocks();
                        }
                    }
                });
    }

    private void setWatchedStocksFromSharedPreferences() {
        String watchedStocks = mSharedPreferencesRepository.getWatchedStocks();
        String[] watchedStocksList = ParseUtility.parseStockList(watchedStocks);
        mHomeView.setWatchedStocks(watchedStocksList);
    }

    @Override
    public void onPreferenceChanged() {
        setWatchedStocksFromSharedPreferences();
    }
}
