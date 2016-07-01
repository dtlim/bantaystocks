package com.dtlim.bantaystocks.home.view;

import com.dtlim.bantaystocks.data.model.Stock;

import java.util.List;

/**
 * Created by dale on 7/1/16.
 */
public interface HomeView {
    void setSubscribedStocks(List<Stock> stocks);
}
