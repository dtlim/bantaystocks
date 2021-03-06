package com.dtlim.bantaystocks.home.presenter;

import com.dtlim.bantaystocks.data.database.Database;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.home.presenter.impl.HomePresenterImpl;
import com.dtlim.bantaystocks.home.view.HomeView;

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
 * Created by dale on 8/2/16.
 */
public class HomePresenterTest {

    private HomePresenterImpl mHomePresenter;
    private HomeView mHomeView;
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
        mHomeView = mock(HomeView.class);
        mDatabaseRepository = mock(DatabaseRepository.class);
        mSharedPreferencesRepository = mock(SharedPreferencesRepository.class);

        mHomePresenter = new HomePresenterImpl(mDatabaseRepository, mSharedPreferencesRepository);
    }

    @Test
    public void testShowSubscribedStockList() {
        mHomePresenter.bindView(mHomeView);

        List<Stock> stocks = DummyModels.getDummyStockList();
        List<Stock> list = new ArrayList<Stock>();
        list.add(stocks.get(0));
        list.add(stocks.get(1));

        when(mSharedPreferencesRepository.getSubscribedStocks()).thenReturn("TEL,MER");
        when(mSharedPreferencesRepository.getWatchedStocks()).thenReturn("TEL,MER");
        when(mDatabaseRepository.queryStocks()).thenReturn(Observable.just(stocks));
        when(mDatabaseRepository.queryStocks("TEL", "MER")).thenReturn(Observable.just(list));
        mHomePresenter.initializeData();

        verify(mHomeView).hideNoSubscribedStocks();
        verify(mHomeView).setSubscribedStocks(list);
    }

    @Test
    public void testShowWatchedStockList() {
        mHomePresenter.bindView(mHomeView);

        List<Stock> stocks = DummyModels.getDummyStockList();
        List<Stock> subscribedList = new ArrayList<Stock>();
        List<Stock> watchedList = new ArrayList<Stock>();

        subscribedList.add(stocks.get(0));
        subscribedList.add(stocks.get(1));
        subscribedList.add(stocks.get(2));

        watchedList.add(stocks.get(0));
        watchedList.add(stocks.get(1));

        when(mSharedPreferencesRepository.getSubscribedStocks()).thenReturn("TEL,MER,2GO");
        when(mSharedPreferencesRepository.getWatchedStocks()).thenReturn("TEL,MER");
        when(mDatabaseRepository.queryStocks()).thenReturn(Observable.just(stocks));
        when(mDatabaseRepository.queryStocks("TEL", "MER", "2GO")).thenReturn(Observable.just(subscribedList));
        when(mDatabaseRepository.queryStocks("TEL", "MER")).thenReturn(Observable.just(watchedList));
        mHomePresenter.initializeData();

        verify(mHomeView).setWatchedStocks(new String[] {"TEL", "MER"});
    }
}
