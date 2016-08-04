package com.dtlim.bantaystocks.select.presenter;

import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.home.presenter.impl.HomePresenterImpl;
import com.dtlim.bantaystocks.home.view.HomeView;
import com.dtlim.bantaystocks.select.presenter.impl.SelectStocksPresenterImpl;
import com.dtlim.bantaystocks.select.view.SelectStocksView;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dale on 8/3/16.
 */
public class SelectPresenterTest {
    private SelectStocksPresenter mSelectStocksPresenter;
    private SelectStocksView mSelectStocksView;
    private DatabaseRepository mDatabaseRepository;
    private SharedPreferencesRepository mSharedPreferencesRepository;

    @BeforeClass
    public static void beforeClass() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Before
    public void beforeEachTest() {
        mSelectStocksView = mock(SelectStocksView.class);
        mDatabaseRepository = mock(DatabaseRepository.class);
        mSharedPreferencesRepository = mock(SharedPreferencesRepository.class);

        mSelectStocksPresenter = new SelectStocksPresenterImpl(mDatabaseRepository, mSharedPreferencesRepository);
    }

    @Test
    public void testShowStockList() {
        mSelectStocksPresenter.bindView(mSelectStocksView);

        List<Stock> stocks = DummyModels.getDummyStockList();
        List<Stock> list = new ArrayList<Stock>();
        list.add(stocks.get(0));
        list.add(stocks.get(1));

        when(mSharedPreferencesRepository.getSubscribedStocks()).thenReturn("TEL,MER");
        when(mSharedPreferencesRepository.getWatchedStocks()).thenReturn("TEL,MER");
        when(mDatabaseRepository.queryStocks()).thenReturn(Observable.just(stocks));
        when(mDatabaseRepository.queryStocks("TEL", "MER")).thenReturn(Observable.just(list));
        mSelectStocksPresenter.initializeDataFromDatabase();

        verify(mSelectStocksView).setStocks(stocks);
    }

    @Test
    public void testShowSubscribedStockList() {
        mSelectStocksPresenter.bindView(mSelectStocksView);

        List<Stock> stocks = DummyModels.getDummyStockList();
        List<Stock> list = new ArrayList<>();
        list.add(stocks.get(0));
        list.add(stocks.get(1));

        when(mSharedPreferencesRepository.getSubscribedStocks()).thenReturn("TEL,MER");
        when(mSharedPreferencesRepository.getWatchedStocks()).thenReturn("TEL,MER");
        when(mDatabaseRepository.queryStocks()).thenReturn(Observable.just(stocks));
        when(mDatabaseRepository.queryStocks("TEL", "MER")).thenReturn(Observable.just(list));
        mSelectStocksPresenter.initializeSubscribedStocks();

        verify(mSelectStocksView).setSubscribedStocks(new String[]{"TEL", "MER"});
    }
}
