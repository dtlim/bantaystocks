package com.dtlim.bantaystocks.data.repository;

import com.dtlim.bantaystocks.BuildConfig;
import com.dtlim.bantaystocks.common.utility.DateUtility;
import com.dtlim.bantaystocks.data.api.StocksApiInterface;
import com.dtlim.bantaystocks.data.model.Price;
import com.dtlim.bantaystocks.data.model.Stock;
import com.dtlim.bantaystocks.data.model.StockList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TimeInterval;

/**
 * Created by dale on 7/15/16.
 */
public class PeriodicRetrofitNotificationRepository implements StocksNotificationRepository {

    private StocksApiInterface mStocksApiInterface;

    public PeriodicRetrofitNotificationRepository() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ?
                        HttpLoggingInterceptor.Level.BODY :
                        HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        mStocksApiInterface = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://phisix-api4.appspot.com")
                .build()
                .create(StocksApiInterface.class);
    }

    @Override
    public void connect(ConnectionListener listener) throws Throwable {
        listener.onConnectSuccess();
    }

    // http://stackoverflow.com/questions/31768184/periodic-http-requests-using-rxjava-and-retrofit
    @Override
    public Observable<List<Stock>> getStocks() {
        return Observable.interval(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).timeInterval()
            .flatMap(new Func1<TimeInterval<Long>, Observable<StockList>>() {
                @Override
                public Observable<StockList> call(TimeInterval<Long> longTimeInterval) {
                    return mStocksApiInterface.getStocks();
                }
            })
                .flatMap(new Func1<StockList, Observable<List<Stock>>>() {
                    @Override
                    public Observable<List<Stock>> call(StockList stockList) {
                        List<Stock> stocks = stockList.getStockList();
                        for(int i=0; i<stocks.size(); i++) {
                            stocks.get(i).setLastUpdate(
                                    DateUtility.parseApiToUnixTimestamp(stockList.getAsOf()));
                        }
                        return Observable.just(stocks);
                    }
                });
    }
}
