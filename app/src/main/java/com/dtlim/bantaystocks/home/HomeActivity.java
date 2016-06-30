package com.dtlim.bantaystocks.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dtlim.bantaystocks.BantayStocksApplication;
import com.dtlim.bantaystocks.R;
import com.dtlim.bantaystocks.dummy.DummyModels;
import com.dtlim.bantaystocks.home.adapter.HomeStocksAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_SYSTEM_ALERT_WINDOW = 1;

    @BindView(R.id.bantaystocks_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bantaystocks_main_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_start_display_service)
    Button mButton;
    
    private HomeStocksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeToolbar();
        initializeList();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToShowStockTicker();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_SYSTEM_ALERT_WINDOW: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((BantayStocksApplication) getApplication()).startStocksDisplayService();
                }
                else {

                }
                break;
            }
        }
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

    // https://developer.android.com/reference/android/Manifest.permission.html#SYSTEM_ALERT_WINDOW
    private void attemptToShowStockTicker() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)) {
                ((BantayStocksApplication) getApplication()).startStocksDisplayService();
            }
            else {
                requestDrawOverOtherAppsPermission();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestDrawOverOtherAppsPermission() {
        String packageName = getApplicationContext().getPackageName();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + packageName));
        startActivity(intent);
    }
}
