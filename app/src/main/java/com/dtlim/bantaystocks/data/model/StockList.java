package com.dtlim.bantaystocks.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dale on 7/4/16.
 */
public class StockList {
    @SerializedName("stock")
    private List<Stock> stockList;
    @SerializedName("as_of")
    private String asOf;

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public String getAsOf() {
        return asOf;
    }

    public void setAsOf(String asOf) {
        this.asOf = asOf;
    }
}
