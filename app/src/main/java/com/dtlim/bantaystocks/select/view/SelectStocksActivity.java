package com.dtlim.bantaystocks.select.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dtlim.bantaystocks.BantayStocksApplication;
import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.select.adapter.SelectStocksAdapter;
import com.dtlim.bantaystocks.select.presenter.SelectStocksPresenter;
import com.dtlim.bantaystocks.select.presenter.impl.SelectStocksPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dale on 7/4/16.
 */
public class SelectStocksActivity extends AppCompatActivity implements SelectStocksView {
    @BindView(R.id.bantaystocks_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bantaystocks_select_recyclerview)
    RecyclerView mRecyclerView;

    private SelectStocksAdapter mSelectAdapter;
    private SelectStocksPresenter mSelectPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bantaystocks_activity_select_stocks);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeList();
        initializePresenter();
    }

    private void initializeToolbar() {
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void initializeList() {
        mSelectAdapter = new SelectStocksAdapter(this);
        mSelectAdapter.setStockList(DummyModels.getDummyStockList());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSelectAdapter);
    }

    private void initializePresenter() {
        DatabaseRepository databaseRepository = BantayStocksApplication.getDatabaseRepository();
        SharedPreferencesRepository sharedPreferencesRepository =
                BantayStocksApplication.getSharedPreferencesRepository();

        mSelectPresenter = new SelectStocksPresenterImpl(databaseRepository, sharedPreferencesRepository);
        mSelectPresenter.bindView(this);
        mSelectPresenter.initializeDataFromDatabase();
    }

    @Override
    public void setStocks(List<Stock> stocks) {
        mSelectAdapter.setStockList(stocks);
    }

    @Override
    public void setSubscribedStocks(String[] subscribedStocks) {
        mSelectAdapter.setSubscribedStocks(subscribedStocks);
    }


}
