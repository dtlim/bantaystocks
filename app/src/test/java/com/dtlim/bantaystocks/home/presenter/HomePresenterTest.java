package com.dtlim.bantaystocks.home.presenter;

import com.dtlim.bantaystocks.data.database.Database;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.home.presenter.impl.HomePresenterImpl;
import com.dtlim.bantaystocks.home.view.HomeView;

import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void beforeEachTest() {
        mHomeView = mock(HomeView.class);
        mDatabaseRepository = mock(DatabaseRepository.class);
        mSharedPreferencesRepository = mock(SharedPreferencesRepository.class);

        mHomePresenter = new HomePresenterImpl(mDatabaseRepository, mSharedPreferencesRepository);

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void insert() {
        mHomePresenter.bindView(mHomeView);

        when(mSharedPreferencesRepository.getSubscribedStocks()).thenReturn("TEL,MER");
        when(mSharedPreferencesRepository.getWatchedStocks()).thenReturn("TEL,MER");
        when(mDatabaseRepository.queryStocks()).thenReturn(Observable.just(DummyModels.getDummyStockList()));
        when(mDatabaseRepository.queryStocks("TEL", "MER")).thenReturn(Observable.just(DummyModels.getDummyStockList()));
        mHomePresenter.initializeData();
        verify(mHomeView).hideNoSubscribedStocks();
    }
}
