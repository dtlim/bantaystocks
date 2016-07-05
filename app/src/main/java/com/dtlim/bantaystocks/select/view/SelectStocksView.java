package com.dtlim.bantaystocks.select.view;

import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

/**
 * Created by dale on 7/4/16.
 */
public interface SelectStocksView {
    void setStocks(List<Stock> stocks);
    void setSubscribedStocks(String[] subscribedStocks);
}
