package com.dtlim.bantaystocks;

import android.app.Application;
import android.app.Service;
import android.content.Intent;

import com.dtlim.bantaystocks.data.service.StocksService;

/**
 * Created by dale on 6/23/16.
 */
public class BantayStocksApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, StocksService.class);
        startService(intent);
    }
}
