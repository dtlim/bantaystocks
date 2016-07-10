package com.dtlim.bantaystocks.home.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.dtlim.bantaystocks.BantayStocksApplication;
import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.data.database.repository.DatabaseRepository;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.repository.SharedPreferencesRepository;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.home.adapter.HomeStocksAdapter;
import com.dtlim.bantaystocks.home.presenter.HomePresenter;
import com.dtlim.bantaystocks.home.presenter.impl.HomePresenterImpl;
import com.dtlim.bantaystocks.select.view.SelectStocksActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeView, HomeStocksAdapter.Listener {

    @BindView(R.id.bantaystocks_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bantaystocks_main_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.bantaystocks_main_fab)
    FloatingActionButton mFloatingActionButton;

    private HomeStocksAdapter mAdapter;
    private HomePresenter mHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bantaystocks_activity_home);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeList();
        initializePresenter();
        initializeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mHomePresenter != null) {
            mHomePresenter.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mHomePresenter != null) {
            mHomePresenter.onActivityPause();
        }
    }

    private void initializeToolbar() {
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void initializeList() {
        mAdapter = new HomeStocksAdapter(this);
        mAdapter.setListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initializePresenter() {
        DatabaseRepository databaseRepository = BantayStocksApplication.getDatabaseRepository();
        SharedPreferencesRepository sharedPreferencesRepository =
                BantayStocksApplication.getSharedPreferencesRepository();
        mHomePresenter = new HomePresenterImpl(databaseRepository, sharedPreferencesRepository);
        mHomePresenter.bindView(this);
    }

    private void initializeListeners() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectStocksActivity();
            }
        });
    }

    @Override
    public void setSubscribedStocks(List<Stock> stocks) {
        mAdapter.setStockList(stocks);
    }

    @Override
    public void setWatchedStocks(String[] stocks) {
        mAdapter.setWatchedStocks(stocks);
    }

    @Override
    public void onClickWatch(Stock stock) {
        attemptToShowHomescreenTicker(stock);
    }

    @Override
    public void onClickRemove(Stock stock) {
        mHomePresenter.removeStock(stock);
    }

    // https://developer.android.com/reference/android/Manifest.permission.html#SYSTEM_ALERT_WINDOW
    private void attemptToShowHomescreenTicker(Stock stock) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)) {
                //((BantayStocksApplication) getApplication()).startStocksDisplayService();
                mHomePresenter.watchStock(stock);
            }
            else {
                requestDrawOverOtherAppsPermission();
            }
        }
        else {
            //((BantayStocksApplication) getApplication()).startStocksDisplayService();
            mHomePresenter.watchStock(stock);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestDrawOverOtherAppsPermission() {
        String packageName = getApplicationContext().getPackageName();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    private void startSelectStocksActivity() {
        Intent intent = new Intent(HomeActivity.this, SelectStocksActivity.class);
        startActivity(intent);
    }
}
