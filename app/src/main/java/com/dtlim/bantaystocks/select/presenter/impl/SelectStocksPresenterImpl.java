package com.dtlim.bantaystocks.select.presenter.impl;

import android.database.Cursor;

import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.home.view.HomeView;
import com.dtlim.bantaystocks.select.presenter.SelectStocksPresenter;
import com.dtlim.bantaystocks.select.view.SelectStocksView;
import com.squareup.sqlbrite.SqlBrite;

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

//        Observable<SqlBrite.Query> stocks = mDatabaseRepository.queryStocks();
//
//        stocks.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<SqlBrite.Query>() {
//                    @Override
//                    public void call(SqlBrite.Query query) {
//                        Cursor cursor = query.run();
//                        List<Stock> list = parseCursor(cursor);
//                        if(list != null && !list.isEmpty()) {
//                            mHomeView.setSubscribedStocks(list);
//                        }
//                    }
//                });
    }

    @Override
    public void bindView(SelectStocksView view) {
        mSelectView = view;
    }



}
