package com.dtlim.bantaystocks.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.home.adapter.HomeStocksAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bantaystocks_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bantaystocks_main_recyclerview)
    RecyclerView mRecyclerView;

    private HomeStocksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeList();
    }

    private void initializeToolbar() {
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void initializeList() {
        mAdapter = new HomeStocksAdapter(this);
        mAdapter.setStockList(DummyModels.getDummyStockList());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
