package com.dtlim.bantaystocks.data.api;

import com.dtlim.bantaystocks.data.model.StockList;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by dale on 8/5/16.
 */
public interface StocksApiInterface {
    String ENDPOINT_STOCKS = "/stocks.json";

    @GET(ENDPOINT_STOCKS)
    Observable<StockList> getStocks();
}
